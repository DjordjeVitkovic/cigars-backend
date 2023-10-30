package com.futing.diary.service;

import com.futing.diary.controller.brand.dto.BrandRequestDTO;
import com.futing.diary.controller.brand.dto.BrandResponseDTO;

import java.io.IOException;

public interface BrandService {

    BrandResponseDTO addBrand(BrandRequestDTO brandRequestDTO) throws IOException;
}
