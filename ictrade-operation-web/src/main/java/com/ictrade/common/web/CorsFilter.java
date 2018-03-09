package com.ictrade.common.web;



/**
 * @author tangrong@yikuyi.com
 * @version 1.0.0
 */
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Access-Control-Allow-Origin", "*");
            /*if (request.getHeader("Access-Control-Request-Method") != null
                    && "OPTIONS".equals(request.getMethod())) {
                response.addHeader("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE");
                response.addHeader("Access-Control-Allow-Headers",
                        "X-Requested-With,Origin,Content-Type, Accept");
            }*/
            filterChain.doFilter(request, response);
    }

}