package kr.hhplus.be.server.common;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class RequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.info("Incoming Request: " + request.getRemoteAddr());

        chain.doFilter(request, response);

        logger.info("Outgoing Response");
    }

}
