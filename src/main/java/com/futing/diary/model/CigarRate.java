package com.futing.diary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cigarRating")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CigarRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer cigarRateId;
  private int cigarRateValue;
  private LocalDateTime created;
  private boolean positive;
  private boolean recommend;
  private String comment;
  private Integer userId;
  @ManyToOne
  @JoinColumn(name = "cigarId", referencedColumnName = "cigarId")
  @JsonIgnore
  private Cigar cigar;

}
