package com.helene.spendbijak.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // get header
        String header = request.getHeader("Authorization");

        // check starts with bearer
        if (header != null && header.startsWith("Bearer ")) {

            // extract token
            String token = header.substring(7);

            // validate
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email, null, List.of()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }

        }

        // let the request in
        filterChain.doFilter(request, response);
    }
}
