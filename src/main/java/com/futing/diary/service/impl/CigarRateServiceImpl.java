package com.futing.diary.service.impl;

import com.futing.diary.controller.cigarrating.dto.CigarRateRequestDTO;
import com.futing.diary.controller.cigarrating.dto.CigarRateUpdateRequestDTO;
import com.futing.diary.exception.BadRequestException;
import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Cigar;
import com.futing.diary.model.CigarRate;
import com.futing.diary.model.User;
import com.futing.diary.repository.CigarRateRepository;
import com.futing.diary.repository.CigarRepository;
import com.futing.diary.repository.UserRepository;
import com.futing.diary.service.CigarRateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.futing.diary.util.Constants.ERROR_CIGAR_NOT_EXIST;
import static com.futing.diary.util.Constants.ERROR_USER_NOT_EXIST;

@Slf4j
@Service
@AllArgsConstructor
public class CigarRateServiceImpl implements CigarRateService {

  private final CigarRateRepository cigarRateRepository;
  private final CigarRepository cigarRepository;
  private final UserRepository userRepository;

  @Override
  public CigarRate addCigarRate(CigarRateRequestDTO cigarRateRequestDTO, String username) {

    Cigar cigar = cigarRepository.findById(cigarRateRequestDTO.getCigarId())
      .orElseThrow(() -> new NotFoundException(ERROR_CIGAR_NOT_EXIST));

    User user = verifyUserExistence(username);

    CigarRate cigarRate = CigarRate.builder()
      .created(LocalDateTime.now())
      .cigar(cigar)
      .userId(user.getUserId())
      .cigarRateValue(cigarRateRequestDTO.getCigarRateValue())
      .comment(cigarRateRequestDTO.getComment())
      .positive(cigarRateRequestDTO.isPositive())
      .recommend(cigarRateRequestDTO.isRecommend())
      .build();

    return cigarRateRepository.save(cigarRate);

  }

  @Override
  public void deleteCigarRate(Integer rateId, String username) {
    CigarRate cigarRate = cigarRateRepository.findCigarRateByCigarRateId(rateId)
      .orElseThrow(() -> new NotFoundException("Cigar rate does not exist"));

    checkIsActionAvailableForUser(cigarRate.getUserId(), verifyUserExistence(username).getUserId());

    cigarRateRepository.deleteById(rateId);

    log.info("Cigar rate for cigar {} deleted by user {}", cigarRate.getCigar().getCigarId(), cigarRate.getUserId());
  }

  @Override
  public CigarRate updateCigarRate(CigarRateUpdateRequestDTO cigarRateUpdateRequestDTO, String username) {
    CigarRate cigarRateToUpdate = cigarRateRepository.findCigarRateByCigarRateId(cigarRateUpdateRequestDTO.getCigarRateId())
      .orElseThrow(() -> new NotFoundException("CigarRate does not exist."));

    checkIsActionAvailableForUser(cigarRateToUpdate.getUserId(), verifyUserExistence(username).getUserId());

    return cigarRateRepository.save(setValues(cigarRateToUpdate, cigarRateUpdateRequestDTO));
  }

  private User verifyUserExistence(String username) {
    return userRepository.findByUsernameOrEmail(username)
      .orElseThrow(() -> new NotFoundException(ERROR_USER_NOT_EXIST));
  }

  private void checkIsActionAvailableForUser(Integer cigarRateUserId, Integer userId) {
    if (!Objects.equals(userId, cigarRateUserId))
      throw new BadRequestException("User is not able to perform action.");
  }

  private CigarRate setValues(CigarRate cigarRateToUpdate, CigarRateUpdateRequestDTO cigarRateUpdateRequestDTO) {
    int requestValue = cigarRateUpdateRequestDTO.getCigarRateValue();
    String requestComment = cigarRateUpdateRequestDTO.getComment();
    boolean requestPositive = cigarRateUpdateRequestDTO.isPositive();
    boolean requestRecommend = cigarRateUpdateRequestDTO.isRecommend();

    if (requestValue != cigarRateToUpdate.getCigarRateValue())
      cigarRateToUpdate.setCigarRateValue(requestValue);
    if (StringUtils.isNotBlank(requestComment))
      cigarRateToUpdate.setComment(requestComment);
    if (requestPositive != cigarRateToUpdate.isPositive())
      cigarRateToUpdate.setPositive(requestPositive);
    if (requestRecommend != cigarRateToUpdate.isRecommend())
      cigarRateToUpdate.setRecommend(requestRecommend);

    return cigarRateToUpdate;
  }
}
