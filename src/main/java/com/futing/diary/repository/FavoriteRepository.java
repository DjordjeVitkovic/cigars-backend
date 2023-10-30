package com.futing.diary.repository;

import com.futing.diary.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
  List<Favorite> findByUserId(Integer userId);

  boolean existsAllByCigarIdAndUserId(Integer cigarId, Integer userId);

  void deleteByCigarIdAndUserId(Integer cigarId, Integer userId);
}
