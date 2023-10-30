package com.futing.diary.controller.auth.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {

  private String username;
  private String email;
  private String firstname;
  private String lastName;
  private LocalDateTime dateOfBirth;

}
