package com.futing.diary.service;

import com.futing.diary.controller.cigarrating.dto.CigarRateRequestDTO;
import com.futing.diary.controller.cigarrating.dto.CigarRateUpdateRequestDTO;
import com.futing.diary.model.CigarRate;

public interface CigarRateService {

  CigarRate addCigarRate(CigarRateRequestDTO cigarRate, String username);

  void deleteCigarRate(Integer rateId, String username);

  CigarRate updateCigarRate(CigarRateUpdateRequestDTO cigarRateUpdateRequestDTO, String username);
}
