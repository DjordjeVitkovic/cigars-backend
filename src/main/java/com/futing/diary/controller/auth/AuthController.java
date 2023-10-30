package com.futing.diary.controller.auth;

import com.futing.diary.controller.auth.dto.*;
import com.futing.diary.service.AuthService;
import com.futing.diary.service.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final EmailSenderService emailSenderService;

  @PostMapping("/register")
  public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
    return ResponseEntity.ok(authService.registerUser(registerDTO));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
    return ResponseEntity.ok(authService.loginUser(loginRequestDTO));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request) {
    return ResponseEntity.ok(authService.refreshToken(request));
  }

  @GetMapping("/verify/{token}")
  public ResponseEntity<Void> verifyAccount(@PathVariable(name = "token") String token) {
    authService.verifyAccountRegistration(token);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/verify/resend/{email}")
  public ResponseEntity<Void> resendVerificationToken(@PathVariable(name = "email") String email) {
    emailSenderService.resendVerificationToken(email);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/forgot-reset-password/{email}")
  public ResponseEntity<Void> sendResetPasswordLink(@PathVariable(name = "email") String email) {
    emailSenderService.sendResetPasswordLink(email);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/forgot-reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    authService.resetForgottenPassword(forgotPasswordDTO);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(Principal principal, @RequestBody ResetPasswordDTO resetPasswordDTO) {
    authService.resetPassword(resetPasswordDTO, principal);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
