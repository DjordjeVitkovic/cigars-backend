package com.futing.diary.config.amqp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMessageDTO {

  private Integer userId;
  private String username;
  private String email;
  private MessageType messageType;
}
