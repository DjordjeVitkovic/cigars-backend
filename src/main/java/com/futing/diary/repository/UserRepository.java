package com.futing.diary.repository;

import com.futing.diary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
  Optional<User> findByUsernameOrEmail(String username);

  Optional<User> findByUserId(Integer userId);
}
