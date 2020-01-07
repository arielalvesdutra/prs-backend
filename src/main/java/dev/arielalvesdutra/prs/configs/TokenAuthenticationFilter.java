package dev.arielalvesdutra.prs.configs;

import dev.arielalvesdutra.prs.entities.User;
import dev.arielalvesdutra.prs.repositories.UserRepository;
import dev.arielalvesdutra.prs.services.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UserRepository userRepository;

    public TokenAuthenticationFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.retrieveToken(request);

        boolean isValidToken = this.tokenService.isValidToken(token);

        if (isValidToken) {
            this.authenticateUser(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token) {

        Long userId = tokenService.getUserId(token);
        User user = userRepository.findById(userId).get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (!this.isValidRequestTokenHeaderFormat(token)) {
            return null;
        }

        return token.substring(7, token.length());
    }

    private Boolean isValidRequestTokenHeaderFormat(String token) {
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }
}
