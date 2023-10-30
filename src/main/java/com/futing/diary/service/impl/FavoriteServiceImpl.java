package com.futing.diary.service.impl;

import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Cigar;
import com.futing.diary.model.Favorite;
import com.futing.diary.model.User;
import com.futing.diary.repository.CigarRepository;
import com.futing.diary.repository.FavoriteRepository;
import com.futing.diary.repository.UserRepository;
import com.futing.diary.service.FavoriteService;
import com.futing.diary.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.futing.diary.util.Constants.ERROR_CIGAR_NOT_EXIST;
import static com.futing.diary.util.Constants.ERROR_USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final CigarRepository cigarRepository;

  @Override
  public List<CigarResponseDTO> getUsersFavorites(String username) {
    User user = verifyUsersExistence(username);

    List<Integer> favoritesCigarIds =
      favoriteRepository.findByUserId(user.getUserId())
        .stream().map(Favorite::getCigarId)
        .toList();

    return cigarRepository.findAllByCigarIdIn(favoritesCigarIds)
      .stream()
      .map(this::convertToCigarResponseDTO)
      .toList();
  }

  @Override
  @Transactional
  public void addOrRemoveFavorite(String username, Integer cigarId) {
    verifyCigarExistence(cigarId);
    User user = userRepository.findByUsernameOrEmail(username).
      orElseThrow(() -> new NotFoundException(ERROR_USER_NOT_EXIST));

    var userId = user.getUserId();
    var exist = favoriteRepository.existsAllByCigarIdAndUserId(cigarId, userId);
    if (exist)
      favoriteRepository.deleteByCigarIdAndUserId(cigarId, userId);
    else
      favoriteRepository.save(Favorite.builder()
        .userId(userId)
        .cigarId(cigarId)
        .created(ZonedDateTime.now())
        .build());
  }

  private void verifyCigarExistence(Integer cigarId) {
    cigarRepository.findById(cigarId)
      .orElseThrow(() -> new NotFoundException(ERROR_CIGAR_NOT_EXIST));
  }

  private User verifyUsersExistence(String username) {
    return userRepository.findByUsernameOrEmail(username)
      .orElseThrow(() -> new NotFoundException(Constants.ERROR_USER_NOT_EXIST));
  }

  private CigarResponseDTO convertToCigarResponseDTO(Cigar cigar) {
    return CigarResponseDTO.builder()
      .cigarId(cigar.getCigarId())
      .name(cigar.getName())
      .wrapper(cigar.getWrapper())
      .binder(cigar.getBinder())
      .filler(cigar.getFiller())
      .cigarStrength(cigar.getCigarStrength())
      .shape(cigar.getShape())
      .rolling(cigar.getRolling())
      .images(cigar.getImages())
      .agingPotential(cigar.isAgingPotential())
      .harvesting(cigar.getHarvesting())
      .curing(cigar.getCuring())
      .aging(cigar.getAging())
      .priceRange(cigar.getPriceRange())
      .limitedEdition(cigar.isLimitedEdition())
      .historicalBackground(cigar.getHistoricalBackground())
      .flavors(cigar.getFlavors())
      .fermentation(cigar.getFermentation())
      .pairings(cigar.getPairings())
      .humidificationStorage(cigar.getHumidificationStorage())
      .brandId(cigar.getBrand() == null ? null : cigar.getBrand().getBrandId())
      .ratings(cigar.getRatings())
      .build();
  }
}
