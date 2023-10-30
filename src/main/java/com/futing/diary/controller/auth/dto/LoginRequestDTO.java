package com.futing.diary.controller.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {

  private String username;
  private String password;

}