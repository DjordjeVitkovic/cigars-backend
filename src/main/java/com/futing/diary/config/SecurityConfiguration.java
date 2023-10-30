package com.futing.diary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationConverter jwtAuthenticationConverter;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
      .cors(Customizer.withDefaults())
      .csrf(scrf -> scrf.disable())
      .authorizeHttpRequests(auth -> {
//        auth.requestMatchers("/v1/auth/**").permitAll();
//        auth.requestMatchers("/v1/admin/**").hasRole("ADMIN");
//        auth.requestMatchers("/v1/user/**").hasAnyRole("USER", "ADMIN");
        auth.anyRequest().permitAll();
      })
      .oauth2ResourceServer(oauth ->
        oauth.jwt(jwt ->
          jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .logout((logout)
        -> logout.logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
      .oauth2Login(Customizer.withDefaults())
      .build();

  }

}
