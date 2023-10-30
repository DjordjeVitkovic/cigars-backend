package com.futing.diary.controller.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDTO {

  private String newPassword;
  private String confirmPassword;
  private String currentPassword;
}
