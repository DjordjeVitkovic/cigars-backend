package com.futing.diary.controller.cigar;

import com.futing.diary.controller.cigar.dto.CigarRequestDTO;
import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.controller.cigar.dto.PageCigarResponseDTO;
import com.futing.diary.service.CigarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/cigar")
@RequiredArgsConstructor
public class CigarController {

  private final CigarService cigarService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CigarResponseDTO> addCigar(@RequestPart(name = "images", required = false) MultipartFile[] images,
                                                   @RequestPart CigarRequestDTO requestDTO) throws IOException {
    return ResponseEntity.ok(cigarService.addCigar(images, requestDTO));
  }

  @GetMapping("/{cigarId}")
  public ResponseEntity<CigarResponseDTO> getCigar(@PathVariable("cigarId") Integer cigarId) {
    return ResponseEntity.ok(cigarService.getCigar(cigarId));
  }

  @GetMapping("/search")
  public ResponseEntity<PageCigarResponseDTO> searchCigars(@RequestParam("search") String search,
                                                           @RequestParam(defaultValue = "20") int pageSize,
                                                           @RequestParam(defaultValue = "0") int pageNumber) {
    return ResponseEntity.ok(cigarService.searchCigar(search, pageSize, pageNumber));
  }

  @DeleteMapping("/{cigarId}")
  public ResponseEntity<Void> deleteCigar(@PathVariable("cigarId") Integer cigarId) {
    cigarService.deleteCigarById(cigarId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
