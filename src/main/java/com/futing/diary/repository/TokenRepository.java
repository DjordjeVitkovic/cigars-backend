package com.futing.diary.repository;

import com.futing.diary.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>
{
  @Query("""
    select t from Token t inner join User u on t.user.userId = u.userId
    where u.userId = :userId and (t.expired = false or t.revoked = false)
    """)
  List<Token> findAllValidTokensByUserId(Integer userId);

  // TODO: 9/22/2023 Write query to return just unrevoked and enabled tokens if needed
  Optional<Token> findByToken(String token);

  @Transactional
  @Modifying
  @Query("""
    update Token t set t.revoked = true, t.expired = true WHERE t.user.userId = :userId AND t.tokenType = 'VERIFICATION'
    """)
  void revokeAllVerificationUserTokens(@Param("userId") Integer userId);

  @Transactional
  @Modifying
  @Query("""
    update Token t set t.revoked = true, t.expired = true WHERE t.user.userId = :userId AND t.tokenType = 'RESET_PASSWORD'
    """)
  void revokeAllResetPasswordUserTokens(@Param("userId") Integer userId);
}
