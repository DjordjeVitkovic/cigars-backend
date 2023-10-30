package com.futing.diary.controller.cigar.dto;

import com.futing.diary.model.CigarRate;
import com.futing.diary.model.Image;
import com.futing.diary.model.enums.CigarRolling;
import com.futing.diary.model.enums.CigarShape;
import com.futing.diary.model.enums.CigarStrength;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CigarResponseDTO {

  private Integer cigarId;
  private String name;
  private String wrapper;
  private String binder;
  private String filler;
  private CigarStrength cigarStrength;
  private CigarShape shape;
  private CigarRolling rolling;
  private List<Image> images;
  private List<CigarRate> ratings;
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
  private Integer brandId;
}
