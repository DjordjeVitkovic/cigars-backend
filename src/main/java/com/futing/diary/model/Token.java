package com.futing.diary.model;

import com.futing.diary.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  private boolean expired;

  private boolean revoked;
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;
}
