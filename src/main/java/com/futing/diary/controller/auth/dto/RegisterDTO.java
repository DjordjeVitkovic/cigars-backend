package com.futing.diary.controller.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class RegisterDTO {

  @NotNull
  @NotEmpty
  private String username;
  @NotNull
  @NotEmpty
  private String password;
  @NotNull
  private String confirmPassword;
  @NotNull
  @Email
  private String email;
  @NotNull
  private String firstname;
  @NotNull
  private String lastName;
  @NotNull
  private LocalDateTime dateOfBirth;
}
