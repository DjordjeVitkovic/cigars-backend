package com.futing.diary.service;

import com.futing.diary.controller.auth.dto.LoginResponseDTO;
import com.futing.diary.controller.auth.dto.UserResponseDTO;
import com.futing.diary.exception.AuthorizationException;
import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Token;
import com.futing.diary.model.User;
import com.futing.diary.model.enums.TokenType;
import com.futing.diary.repository.TokenRepository;
import com.futing.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  @Value("${jwt.access_token_lifespan}")
  private long accessTokenLifespan;

  @Value("${jwt.refresh_token_lifespan}")
  private long refreshTokenLifespan;

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final TokenRepository tokenRepository;
  private final Clock clock;
  private final UserRepository userRepository;

  public LoginResponseDTO generateTokens(Authentication authentication, String refreshToken, User user) {

    Instant now = Instant.now();

    String scope = authentication.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(" "));

    String newAccessToken = generateAccessToken(authentication, now, scope);
    String newRefreshToken = generateRefreshToken(authentication, refreshToken, now);

    revokeAllUserTokens(user);
    saveToken(user, newAccessToken);

    return LoginResponseDTO.builder()
      .access_token(newAccessToken)
      .refresh_token(newRefreshToken)
      .user(UserResponseDTO.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .firstname(user.getFirstname())
        .lastName(user.getLastName())
        .dateOfBirth(user.getDateOfBirth())
        .build())
      .build();
  }

  private String generateAccessToken(Authentication authentication, Instant now, String scope) {
    JwtClaimsSet accessToken = JwtClaimsSet.builder()
      .issuer("self")
      .issuedAt(now)
      .subject(authentication.getName())
      .claim("roles", scope)
      .expiresAt(now.plus(accessTokenLifespan, ChronoUnit.SECONDS))
      .id(UUID.randomUUID().toString())
      .claim("typ", "Access")
      .build();
    return jwtEncoder.encode(JwtEncoderParameters.from(accessToken)).getTokenValue();
  }

  private String generateRefreshToken(Authentication authentication, String refreshToken, Instant now) {
    String newRefreshToken = refreshToken;
    // if refresh token is valid return the same one
    if (refreshToken == null || jwtDecoder.decode(refreshToken).getExpiresAt().isBefore(now)) {
      JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .subject(authentication.getName())
        .expiresAt(now.plus(refreshTokenLifespan, ChronoUnit.SECONDS))
        .id(UUID.randomUUID().toString())
        .claim("typ", "Refresh")
        .build();
      newRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();
    }
    return newRefreshToken;
  }

  public Token generateVerificationToken(Integer userId) {
    Token token = Token.builder()
      .user(userRepository.findByUserId(userId).get())
      .token(UUID.randomUUID().toString())
      .tokenType(TokenType.VERIFICATION)
      .expired(false)
      .revoked(false)
      .createdAt(LocalDateTime.now())
      .build();

    tokenRepository.revokeAllVerificationUserTokens(userId);

    return tokenRepository.save(token);
  }

  public Token generateResetPasswordToken(Integer userId) {
    Token token = Token.builder()
      .user(userRepository.findByUserId(userId).get())
      .token(UUID.randomUUID().toString())
      .tokenType(TokenType.RESET_PASSWORD)
      .expired(false)
      .revoked(false)
      .createdAt(LocalDateTime.now())
      .build();

    tokenRepository.revokeAllResetPasswordUserTokens(userId);

    return tokenRepository.save(token);
  }

  private void saveToken(User savedUser, String jwtToken) {
    var token = Token.builder()
      .user(savedUser)
      .token(jwtToken)
      .tokenType(TokenType.BEARER)
      .revoked(false)
      .expired(false)
      .createdAt(LocalDateTime.now())
      .build();
    tokenRepository.save(token);
    log.debug("Access token generated.");
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getUserId());

    if (validUserTokens.isEmpty())
      return;

    validUserTokens.forEach(
      token -> {
        token.setExpired(true);
        token.setRevoked(true);
      }
    );
    tokenRepository.saveAll(validUserTokens);
    log.debug("All tokens for user {} are revoked.", user.getUsername());
  }

  public Optional<Token> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public String extractUsername(String jwt) {
    try {
      return jwtDecoder.decode(jwt).getSubject();
    } catch (Exception e) {
      throw new AuthorizationException("Unauthorized");
    }
  }

  public boolean isTokenValid(String jwt, User userDetails) {
    var username = extractUsername(jwt);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
  }

  public Token isVerificationTokenValid(String verificationToken) {
    Token token = tokenRepository.findByToken(verificationToken)
      .orElseThrow(() -> new NotFoundException("Verification token not found."));

    if (token.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now()) || token.isRevoked()) {
      invalidateToken(token);
      throw new IllegalStateException("Verification token not valid.");
    }
    invalidateToken(token);

    return token;
  }

  private void invalidateToken(Token token) {
    token.setRevoked(true);
    token.setExpired(true);
    tokenRepository.save(token);
  }

  public boolean isRefreshToken(String jwt) {
    return jwtDecoder.decode(jwt).getClaims().get("typ").equals("Refresh");
  }

  private boolean isTokenExpired(String jwt) {
    return jwtDecoder.decode(jwt).getExpiresAt().isBefore(clock.instant());
  }

}
