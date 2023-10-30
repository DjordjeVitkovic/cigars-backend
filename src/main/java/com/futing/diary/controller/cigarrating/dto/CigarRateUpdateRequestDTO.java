package com.futing.diary.controller.cigarrating.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CigarRateUpdateRequestDTO {

  private Integer cigarRateId;
  private int cigarRateValue;
  private boolean positive;
  private boolean recommend;
  private String comment;
}
