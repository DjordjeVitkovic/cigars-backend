package com.futing.diary.controller.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequestDTO {

    private String brandName;
    private String countryOfOrigin;
    private String description;
    private String website;
    private String founder;
    private Integer yearEstablished;
    //private String logoImageUrl;
}
