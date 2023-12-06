package com.ets.gti619.authmodule.security;

import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserLoginProtectionFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/login");

    private UserLoginProtectionsService userLoginProtectionsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String username = request.getParameter("username");
        int secondsBefore = userLoginProtectionsService.getSecondsBeforeNextLoginAttempt(username);
        if (secondsBefore > 0) {
            response.sendRedirect("/too-many-logins?seconds=" + secondsBefore);
            return;
        }
        
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        RequestMatcher negated = new NegatedRequestMatcher(matcher);
        return negated.matches(request);
    }
}
