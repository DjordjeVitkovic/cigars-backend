package com.futing.diary.service.impl;

import com.futing.diary.controller.brand.dto.BrandRequestDTO;
import com.futing.diary.controller.brand.dto.BrandResponseDTO;
import com.futing.diary.model.Brand;
import com.futing.diary.repository.BrandRepository;
import com.futing.diary.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public BrandResponseDTO addBrand(BrandRequestDTO brandRequestDTO) {
        Brand brand = convertToBrandEntity(brandRequestDTO);
        Brand savedBrand = brandRepository.save(brand);
        log.info("Created brand with name {}", brand.getBrandName());
        return convertToBrandDTO(savedBrand);
    }

    private BrandResponseDTO convertToBrandDTO(Brand brand) {
        return BrandResponseDTO.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .countryOfOrigin(brand.getCountryOfOrigin())
                .description(brand.getDescription())
                .website(brand.getWebsite())
                .founder(brand.getFounder())
                .yearEstablished(brand.getYearEstablished())
                .build();
    }

    private Brand convertToBrandEntity(BrandRequestDTO brandRequestDTO) {
        return Brand.builder()
                .brandName(brandRequestDTO.getBrandName())
                .countryOfOrigin(brandRequestDTO.getCountryOfOrigin())
                .description(brandRequestDTO.getDescription())
                .website(brandRequestDTO.getWebsite())
                .founder(brandRequestDTO.getFounder())
                .yearEstablished(brandRequestDTO.getYearEstablished())
                .build();
    }
}
