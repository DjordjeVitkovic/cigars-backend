package com.futing.diary.controller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

  private UserResponseDTO user;
  private String access_token;
  private String refresh_token;
}
