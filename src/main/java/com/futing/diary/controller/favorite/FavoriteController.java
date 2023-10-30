package com.futing.diary.controller.favorite;

import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.exception.AuthorizationException;
import com.futing.diary.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

  private final FavoriteService favoriteService;

  @GetMapping
  ResponseEntity<List<CigarResponseDTO>> getUsersFavorites(Principal principal) {
    if (principal == null)
      throw new AuthorizationException("Forbidden");
    return ResponseEntity.ok(favoriteService.getUsersFavorites(principal.getName()));
  }

  @PostMapping("/{cigarId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> markOrUnmarkCigarAsFavorite(Principal principal, @PathVariable("cigarId") Integer cigarId) {
    favoriteService.addOrRemoveFavorite(principal.getName(), cigarId);
    return ResponseEntity.accepted().build();
  }
}
