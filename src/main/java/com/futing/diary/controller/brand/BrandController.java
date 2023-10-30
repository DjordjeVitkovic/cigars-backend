package com.futing.diary.controller.brand;


import com.futing.diary.controller.brand.dto.BrandRequestDTO;
import com.futing.diary.controller.brand.dto.BrandResponseDTO;
import com.futing.diary.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping()
    public ResponseEntity<BrandResponseDTO> addBrand(@RequestBody BrandRequestDTO brandRequestDTO) throws IOException {
        return ResponseEntity.ok(brandService.addBrand(brandRequestDTO));
    }
}
