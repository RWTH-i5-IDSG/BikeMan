package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.domain.login.Authority;
import de.rwth.idsg.bikeman.domain.login.PersistentToken;
import de.rwth.idsg.bikeman.domain.login.User;
import de.rwth.idsg.bikeman.repository.PersistentTokenRepository;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.service.UserService;
import de.rwth.idsg.bikeman.web.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AccountResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    /**
     * GET  /rest/authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/rest/authenticate",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/rest/account",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public UserDTO getAccount(HttpServletResponse response) {
        User user = userService.getUserWithAuthorities();
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
		List<String> roles = new ArrayList<>();
		for (Authority authority : user.getAuthorities()) {
		    roles.add(authority.getName());
		}

        return new UserDTO(user.getLogin(), roles);
    }

    /**
     * POST  /rest/account -> update the current user information.
     */
    @RequestMapping(value = "/rest/account",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void saveAccount(@RequestBody UserDTO userDTO) throws IOException {
        userService.updateUserInformation(userDTO.getLogin());
    }

    /**
     * POST  /rest/change_password -> changes the current user's password
     */
    @RequestMapping(value = "/rest/account/change_password",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void changePassword(@RequestBody String password, HttpServletResponse response) throws IOException {
        if (password == null || password.equals("")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Password should not be empty");
        } else {
            userService.changePassword(password);
        }
    }

    /**
     * GET  /rest/account/sessions -> get the current open sessions.
     */
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = "/rest/account/sessions",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<PersistentToken> getCurrentSessions(HttpServletResponse response) {
//        User user = userRepository.findByLogin(SecurityUtils.getCurrentLogin());
//        if (user == null) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//        return persistentTokenRepository.findByUser(user);

        return persistentTokenRepository.findAll();
    }

    /**
     * DELETE  /rest/account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     */
    @RequestMapping(value = "/rest/account/sessions/{series}",
            method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series, HttpServletRequest request) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");

        User user = userRepository.findOne(SecurityUtils.getCurrentLogin());
        List<PersistentToken> persistentTokens = persistentTokenRepository.findByUser(user);
        for (PersistentToken persistentToken : persistentTokens) {
		    if (StringUtils.equals(persistentToken.getSeries(), decodedSeries)) {
                persistentTokenRepository.delete(decodedSeries);
			}
        }
    }
}
