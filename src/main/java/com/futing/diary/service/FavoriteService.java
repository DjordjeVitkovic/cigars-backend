package com.futing.diary.service;

import com.futing.diary.controller.cigar.dto.CigarResponseDTO;

import java.util.List;

public interface FavoriteService {
  List<CigarResponseDTO> getUsersFavorites(String username);

  void addOrRemoveFavorite(String username, Integer cigarId);
}
