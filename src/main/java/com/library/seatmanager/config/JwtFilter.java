package com.library.seatmanager.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        // allow auth endpoints
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Token");
            return;
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        String username = jwtUtil.extractUsername(token);

        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}