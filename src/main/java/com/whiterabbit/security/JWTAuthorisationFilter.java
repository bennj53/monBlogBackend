package com.whiterabbit.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorisationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*
        *
        *
        * */
        String jwt = httpServletRequest.getHeader("Authorization");
        if(jwt == null) throw new RuntimeException("Not Authorized");
        /*
        *
        *
        *
        * */
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
