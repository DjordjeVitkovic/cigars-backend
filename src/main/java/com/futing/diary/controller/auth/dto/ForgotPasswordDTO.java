package com.futing.diary.controller.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordDTO {

  private String newPassword;
  private String confirmPassword;
  private String token;
}
