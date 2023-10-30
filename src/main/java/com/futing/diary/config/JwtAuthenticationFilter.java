package com.futing.diary.config;

import com.futing.diary.model.User;
import com.futing.diary.repository.UserRepository;
import com.futing.diary.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;
  private final TokenService tokenService;
  @Qualifier("handlerExceptionResolver")
  private final HandlerExceptionResolver handlerExceptionResolver;

  public JwtAuthenticationFilter(UserRepository userRepository, TokenService tokenService, HandlerExceptionResolver handlerExceptionResolver) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;
    try {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }
      jwt = authHeader.replace("Bearer ", "");
      username = tokenService.extractUsername(jwt);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && !tokenService.isRefreshToken(jwt)) {
        User user = this.userRepository.findByUsernameOrEmail(username)
          .orElseThrow((() -> new UsernameNotFoundException("User not found")));

        boolean isTokenValid = tokenService.findByToken(jwt)
          .map(token -> !token.isRevoked())
          .orElse(false);

        if (!tokenService.isTokenValid(jwt, user) || !isTokenValid) {
          throw new AuthenticationException("Token is expired or revoked. User unauthorized.");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          user,
          null,
          user.getAuthorities()
        );
        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      handlerExceptionResolver.resolveException(request, response, null, ex);
    }
  }
}
