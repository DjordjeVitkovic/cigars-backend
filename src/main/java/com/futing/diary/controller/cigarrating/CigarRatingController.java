package com.futing.diary.controller.cigarrating;


import com.futing.diary.controller.cigarrating.dto.CigarRateRequestDTO;
import com.futing.diary.controller.cigarrating.dto.CigarRateUpdateRequestDTO;
import com.futing.diary.model.CigarRate;
import com.futing.diary.service.CigarRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/cigar/rating")
@RequiredArgsConstructor
public class CigarRatingController {

  private final CigarRateService cigarRateService;

  @PostMapping
  public ResponseEntity<CigarRate> addCigarRate(Principal principal, @RequestBody CigarRateRequestDTO cigarRate) {
    return ResponseEntity.ok(cigarRateService.addCigarRate(cigarRate, principal.getName()));
  }

  @DeleteMapping("/{rateId}")
  public ResponseEntity<Void> deleteCigarRate(Principal principal, @PathVariable("rateId") Integer rateId) {
    cigarRateService.deleteCigarRate(rateId, principal.getName());
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @PutMapping
  public ResponseEntity<CigarRate> updateCigarRate(Principal principal, @RequestBody CigarRateUpdateRequestDTO cigarRateUpdateRequestDTO) {
    return ResponseEntity.ok(cigarRateService.updateCigarRate(cigarRateUpdateRequestDTO, principal.getName()));
  }
}
