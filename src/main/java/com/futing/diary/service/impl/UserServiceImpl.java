package com.futing.diary.service.impl;

import com.futing.diary.repository.UserRepository;
import com.futing.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.futing.diary.util.Constants.ERROR_USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsernameOrEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_EXIST));
  }
}
