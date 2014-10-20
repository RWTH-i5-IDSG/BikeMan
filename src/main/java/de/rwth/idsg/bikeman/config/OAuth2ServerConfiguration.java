package de.rwth.idsg.bikeman.config;

import de.rwth.idsg.bikeman.security.AjaxLogoutSuccessHandler;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import de.rwth.idsg.bikeman.security.Http401UnauthorizedEntryPoint;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class OAuth2ServerConfiguration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Inject
        private Http401UnauthorizedEntryPoint authenticationEntryPoint;

        @Inject
        private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutUrl("/app/logout")
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .and()
                    .csrf()
                    .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                    .disable()
                    .headers()
                    .frameOptions().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/views/**").permitAll()
                    .antMatchers("/app/rest/authenticate").permitAll()
                    // TODO: currently station access is unprotected.
                    .antMatchers("/psi*").permitAll()
                    .antMatchers("/app/rest/logs/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/app/rest/audits*").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/audits/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/customers*").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/customers/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/managers*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/app/rest/managers/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/app/rest/pedelecs*").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/pedelecs/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/stations*").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/stations/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/transactions*").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/transactions/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/rest/cardaccount/**").hasAuthority(AuthoritiesConstants.MANAGER)
                    .antMatchers("/app/**").authenticated()
                    .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/websocket/**").permitAll()
                    .antMatchers("/metrics*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/health*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/dump*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/shutdown*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/beans*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/info*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/autoconfig*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/env*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace*").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/protected/**").authenticated();

        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {

        private static final String ENV_OAUTH = "authentication.oauth.";
        private static final String PROP_CLIENTID = "clientid";
        private static final String PROP_SECRET = "secret";
        private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

        private RelaxedPropertyResolver propertyResolver;

        @Inject
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Inject
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient(propertyResolver.getProperty(PROP_CLIENTID))
                    .scopes("read", "write")
                    .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MANAGER, AuthoritiesConstants.CUSTOMER)
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                    .secret(propertyResolver.getProperty(PROP_SECRET))
                    .accessTokenValiditySeconds(propertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS, Integer.class, 18000));
        }


        @Override
        public void setEnvironment(Environment environment) {
            this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
        }
    }
}
