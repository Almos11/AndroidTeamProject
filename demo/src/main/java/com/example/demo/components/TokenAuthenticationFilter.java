package com.example.demo.components;

import java.io.IOException;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.ArrayList;
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userDataBaseRepository;
    int tokenExpirationSeconds = 36000;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws
            jakarta.servlet.ServletException, IOException {
        String requestName = request.getRequestURI();
        if (requestName.equals("/login") || requestName.equals("/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        UserDataBase user = userDataBaseRepository.findByToken(token);
        if (user != null && !user.isTokenExpired(tokenExpirationSeconds)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,
                    null, new ArrayList<>()));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

