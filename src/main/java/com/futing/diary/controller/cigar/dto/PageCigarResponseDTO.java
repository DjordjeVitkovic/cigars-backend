package com.futing.diary.controller.cigar.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageCigarResponseDTO {

  private List<CigarResponseDTO> cigars;
  private int totalPages;
  private long totalElements;
}
