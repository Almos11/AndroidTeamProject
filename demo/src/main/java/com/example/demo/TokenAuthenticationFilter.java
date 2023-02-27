package com.example.demo;

import java.io.IOException;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.ArrayList;
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDataBaseRepository userDataBaseRepository;

    private static final String AUTH_HEADER = "Authorization";

    //действие токена в днях
    int tokenExpirationDays = 30;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        UserDataBase user = userDataBaseRepository.findByUsername(authHeader);
        if (user != null && !user.isTokenExpired(tokenExpirationDays)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
        }
        filterChain.doFilter(request, response);
    }
}

