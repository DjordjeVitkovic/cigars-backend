package com.futing.diary.controller.cigar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.futing.diary.model.enums.CigarRolling;
import com.futing.diary.model.enums.CigarShape;
import com.futing.diary.model.enums.CigarStrength;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CigarRequestDTO {

  @NotNull
  private String name;
  private String wrapper;
  private String binder;
  private String filler;
  private CigarStrength cigarStrength;
  private CigarShape shape;
  private CigarRolling rolling;
  private boolean agingPotential;
  private String harvesting;
  private String curing;
  private String aging;
  private int priceRange;
  private boolean limitedEdition;
  private String historicalBackground;
  private String flavors;
  private String fermentation;
  private String pairings;
  private String humidificationStorage;
  private int brandId;

}
