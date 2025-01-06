package com.example.demo.controller.filter;

import com.example.demo.service.TestService;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final TestService testService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, @Qualifier("First") TestService testService) {
        this.jwtUtil = jwtUtil;
        this.testService = testService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(token);
        } else {
            //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error in authHeader");
            //return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = testService.loadUserByUsername(username).orElseThrow(RuntimeException::new); //TODO change it
            if (jwtUtil.validateToken(token)) {
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is not valid");
                //return;
            }
        } else {
            //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Username is miss");
            //return;
        }

        filterChain.doFilter(request, response);
    }
}