package de.rwth.idsg.bikeman.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix="server.cors")
public class CORSFilter implements Filter {

    private List<String> allowedDomains = new ArrayList<String>();

    public List<String> getAllowedDomains () {
        return this.allowedDomains;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ( !(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse) ) {
            throw new ServletException("Non-http requests are not supported!");
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String origin = httpRequest.getHeader("origin");
        if (origin != null) {
            if (this.getAllowedDomains().contains(origin)) {
                httpResponse.setHeader("Access-Control-Allow-Origin", origin);
                httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpResponse.setHeader("Access-Control-Max-Age", "3600");
                httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    public void destroy() {}
}

