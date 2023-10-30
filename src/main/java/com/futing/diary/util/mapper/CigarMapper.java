package com.futing.diary.util.mapper;

import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.model.Cigar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CigarMapper {

  @Mapping(target = "brandId", source = "brand.brandId")
  CigarResponseDTO cigarToCigarResponseDTO(Cigar cigar);
}
