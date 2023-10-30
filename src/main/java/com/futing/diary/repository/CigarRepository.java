package com.futing.diary.repository;

import com.futing.diary.model.Cigar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CigarRepository extends JpaRepository<Cigar, Integer> {

  @Query("SELECT c FROM Cigar c WHERE " +
    "c.name LIKE CONCAT('%',:search, '%')")
  Page<Cigar> searchCigars(String search, Pageable pageable);

  List<Cigar> findAllByCigarIdIn(List<Integer> cigarIds);

}
