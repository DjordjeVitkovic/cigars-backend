package com.futing.diary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.ZonedDateTime;


@Data
@Builder
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "roleId")
  private Integer roleId;

  @Column(name = "authority")
  private String authority;

  @Column(name = "created")
  private ZonedDateTime created;

  @Override
  public String getAuthority() {
    return this.authority;
  }
}
