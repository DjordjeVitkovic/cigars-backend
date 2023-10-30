package com.futing.diary.service;

import com.futing.diary.controller.cigar.dto.CigarRequestDTO;
import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.controller.cigar.dto.PageCigarResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CigarService {

  CigarResponseDTO addCigar(MultipartFile[] images, CigarRequestDTO requestDTO) throws IOException;

  CigarResponseDTO getCigar(Integer cigarId);

  void deleteCigarById(Integer cigarId);

  PageCigarResponseDTO searchCigar(String search, int pageSize, int pageNumber);
}
