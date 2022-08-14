package com.obervatorio_pedagogico.backend.infrastructure.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.obervatorio_pedagogico.backend.infrastructure.security.auth.JwtUtils;
import com.obervatorio_pedagogico.backend.infrastructure.security.service.SecurityService;

import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtFilterRequest extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    private SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                username = jwtUtils.extractEmail(jwtToken);

            } catch (MalformedJwtException e) {
                username = null;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails currentUserDetails = securityService.loadUserByUsername(username);
            Boolean tokenValidation = jwtUtils.validateToken(jwtToken, currentUserDetails);
            if (tokenValidation) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, currentUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
