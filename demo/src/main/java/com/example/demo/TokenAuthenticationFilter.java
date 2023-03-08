package com.example.demo;

import java.io.IOException;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.ArrayList;
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userDataBaseRepository;
    int tokenExpirationSeconds = 3600;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws
            jakarta.servlet.ServletException, IOException {
        String requestName = request.getRequestURI();
        if (requestName.equals("/login") || requestName.equals("/upload")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getParameter("token");
        UserDataBase user = userDataBaseRepository.findByToken(token);
        if (user != null && !user.isTokenExpired(tokenExpirationSeconds)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,
                    null, new ArrayList<>()));
        }
        filterChain.doFilter(request, response);
    }
}

