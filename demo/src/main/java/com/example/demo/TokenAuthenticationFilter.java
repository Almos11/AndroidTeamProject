package com.example.demo;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.ArrayList;
import static com.example.demo.MyController.usersWhoHaveToken;
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";

    //действие токена в днях
    int tokenExpirationDays = 30;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        User user = usersWhoHaveToken.get(authHeader);
        if (user != null && !user.isTokenExpired(tokenExpirationDays)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
        }
        filterChain.doFilter(request, response);
    }
}

