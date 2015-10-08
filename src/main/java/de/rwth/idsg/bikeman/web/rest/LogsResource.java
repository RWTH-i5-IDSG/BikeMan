package de.rwth.idsg.bikeman.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.config.Constants;
import de.rwth.idsg.bikeman.web.rest.dto.LoggerDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LogsResource {

    @Inject private Environment env;

    private Path logPath;

    @PostConstruct
    private void init() {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            logPath = Paths.get(System.getProperty("catalina.base"), "logs", "bikeman.log");
        }
    }

    @Timed
    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoggerDTO> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList()
                      .stream()
                      .map(LoggerDTO::new)
                      .collect(Collectors.toList());
    }

    @Timed
    @RequestMapping(value = "/logs", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }

    /**
     * Prints the application logs
     */
    @RequestMapping(value = "/logs/bikeman", method = RequestMethod.GET)
    public void log(HttpServletResponse response) {
        response.setContentType("text/plain");

        try (PrintWriter writer = response.getWriter()) {
            if (logPath == null) {
                writer.write("Not available, because BikeMan is not running in '"
                        + Constants.SPRING_PROFILE_PRODUCTION + "' profile");
            } else {
                Files.lines(logPath, StandardCharsets.UTF_8)
                     .forEach(writer::println);
            }
        } catch (IOException e) {
            log.error("Exception happened", e);
        }
    }
}
