package com.neosrate.neosrate.configuration;

import com.neosrate.neosrate.repository.UserRepository;
import com.neosrate.neosrate.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    final
    TokenService tokenService;

    final
    UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var url = request.getRequestURI();

            String[] parts = url.split("/");

            String lastParam = parts[parts.length - 1];

            if (Objects.equals(tokenService.getUserIdFromJwtToken(token), lastParam)) {
                var login = tokenService.validateToken(token);
                UserDetails user = userRepository.findByEmail(login);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        System.out.println("TOKEN IS NULL");

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
