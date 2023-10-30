package com.futing.diary.service;

import com.futing.diary.config.amqp.RegistrationPublisher;
import com.futing.diary.config.amqp.dto.MessageType;
import com.futing.diary.config.amqp.dto.RegisterMessageDTO;
import com.futing.diary.controller.auth.dto.*;
import com.futing.diary.exception.BadRequestException;
import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Role;
import com.futing.diary.model.Token;
import com.futing.diary.model.User;
import com.futing.diary.repository.RoleRepository;
import com.futing.diary.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.futing.diary.util.Constants.ERROR_ROLE_NOT_EXIST;
import static com.futing.diary.util.Constants.ERROR_USER_NOT_EXIST;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final JwtDecoder jwtDecoder;
  private final RegistrationPublisher registrationPublisher;

  public UserResponseDTO registerUser(RegisterDTO registerDTO) {

    validateRegistrationRequest(registerDTO);

    User savedUser = saveUser(registerDTO);
    registrationPublisher.publishSuccessRegistration(createRegistrationAMQPMessage(savedUser));

    return UserResponseDTO.builder()
      .username(savedUser.getUsername())
      .email(savedUser.getEmail())
      .dateOfBirth(savedUser.getDateOfBirth())
      .firstname(savedUser.getFirstname())
      .lastName(savedUser.getLastName())
      .build();
  }

  private RegisterMessageDTO createRegistrationAMQPMessage(User savedUser) {
    return RegisterMessageDTO.builder()
      .userId(savedUser.getUserId())
      .email(savedUser.getEmail())
      .username(savedUser.getUsername())
      .messageType(MessageType.REGISTRATION)
      .build();
  }

  private User saveUser(RegisterDTO registerDTO) {
    Role userRole = roleRepository.findByAuthority("USER")
      .orElseThrow(() -> new NotFoundException(ERROR_ROLE_NOT_EXIST));

    Set<Role> authorities = new HashSet<>();
    authorities.add(userRole);

    User userToSave = User.builder()
      .username(registerDTO.getUsername())
      .email(registerDTO.getEmail())
      .password(passwordEncoder.encode(registerDTO.getPassword()))
      .created(ZonedDateTime.now())
      .firstname(registerDTO.getFirstname())
      .lastName(registerDTO.getLastName())
      .dateOfBirth(registerDTO.getDateOfBirth())
      .authorities(authorities)
      .build();

    userRepository.save(userToSave);
    log.info("User with username: {} registered.", registerDTO.getUsername());
    return userToSave;
  }

  private void validateRegistrationRequest(RegisterDTO registerDTO) {
    if (userRepository.findByUsernameOrEmail(registerDTO.getUsername()).isPresent())
      throw new BadRequestException("Username or email already taken.");
    if (userRepository.findByUsernameOrEmail(registerDTO.getEmail()).isPresent())
      throw new BadRequestException("Username or email already taken.");
    if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword()))
      throw new BadRequestException("Passwords are not same");
    // Check if the user is younger than 18 years old
    LocalDateTime dateOfBirth = registerDTO.getDateOfBirth();
    if (dateOfBirth != null) {
      LocalDate birthDate = dateOfBirth.toLocalDate();
      LocalDate currentDate = LocalDate.now();
      if (Period.between(birthDate, currentDate).getYears() < 18) {
        throw new BadRequestException("User must be at least 18 years old.");
      }
    } else {
      throw new BadRequestException("Date of birth is required.");
    }
  }

  public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
    try {
      log.info("Login action is performing for user {}", loginRequestDTO.getUsername());
      Authentication auth =
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

      User user = userRepository.findByUsernameOrEmail(loginRequestDTO.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_EXIST));

      return tokenService.generateTokens(auth, null, user);

    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid credentials");
    }
  }


  public LoginResponseDTO refreshToken(HttpServletRequest request) {

    final String authHeader = request.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalStateException("Token not present.");
    }
    refreshToken = authHeader.replace("Bearer ", "");
    userEmail = validateAndGetEmail(refreshToken);

    if (userEmail != null) {
      var user = this.userRepository.findByUsernameOrEmail(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_EXIST));

      Authentication authentication = (Authentication) request.getUserPrincipal();
      return tokenService.generateTokens(authentication, refreshToken, user);
    } else {
      throw new BadCredentialsException("Invalid credentials");
    }
  }

  private String validateAndGetEmail(String refreshToken) {
    String username;
    try {
      Jwt jwt = jwtDecoder.decode(refreshToken);
      username = jwt.getClaimAsString(JwtClaimNames.SUB);

      if (!jwt.getClaims().get("typ").equals("Refresh")) {
        throw new IllegalArgumentException("Refresh Token is not valid.");
      }

    } catch (JwtException e) {
      throw new JwtException("Token is not valid.");
    }

    return username;
  }

  public void verifyAccountRegistration(String verificationToken) {
    Token token = tokenService.isVerificationTokenValid(verificationToken);
    User user = token.getUser();
    user.setEnabled(true);
    userRepository.save(user);
  }

  public Integer getUserIdByVerificationToken(String token) {
    Token storedToken = tokenService.isVerificationTokenValid(token);

    if (storedToken.isExpired() || storedToken.isRevoked())
      return storedToken.getUser().getUserId();

    return null;
  }

  public void resetForgottenPassword(ForgotPasswordDTO forgotPasswordDTO) {
    Integer userId = getUserIdByVerificationToken(forgotPasswordDTO.getToken());

    if (userId == null)
      throw new BadRequestException("Reset password token is not valid");

    User user = userRepository.findByUserId(userId)
      .orElseThrow(() -> new NotFoundException("User not found."));

    setUserPassword(user, forgotPasswordDTO.getNewPassword());
  }

  public void resetPassword(ResetPasswordDTO resetPasswordDTO, Principal principal) {
    var user = userRepository.findByUsernameOrEmail(principal.getName())
      .orElseThrow(() -> new NotFoundException(ERROR_USER_NOT_EXIST));

    if (!passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), user.getPassword()))
      throw new BadRequestException("Not able to change password.");

    if (!resetPasswordDTO.getNewPassword().equalsIgnoreCase(resetPasswordDTO.getConfirmPassword()))
      throw new BadRequestException("Passwords are not the same.");

    setUserPassword(user, resetPasswordDTO.getNewPassword());
  }

  private void setUserPassword(User user, String resetPasswordDTO) {
    user.setPassword(passwordEncoder.encode(resetPasswordDTO));
    userRepository.save(user);
  }
}
