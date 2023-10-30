package com.futing.diary.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "users")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "userId")
  private Integer userId;
  private String username;
  private String password;
  private String email;
  private ZonedDateTime created;
  private String firstname;
  private String lastName;
  private boolean enabled;
  private LocalDateTime dateOfBirth;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_role_junction",
    joinColumns = {@JoinColumn(name = "userId")},
    inverseJoinColumns = {@JoinColumn(name = "roleId")}
  )
  private Set<Role> authorities;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}
