package com.futing.diary.repository;

import com.futing.diary.model.CigarRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CigarRateRepository extends JpaRepository<CigarRate, Integer> {

  Optional<CigarRate> findCigarRateByCigarRateId(Integer cigarRateId);
}
