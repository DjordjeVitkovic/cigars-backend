package com.futing.diary.model;

import com.futing.diary.model.enums.CigarRolling;
import com.futing.diary.model.enums.CigarShape;
import com.futing.diary.model.enums.CigarStrength;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "cigars")
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cigar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer cigarId;
  private String name;
  private String wrapper;
  private String binder;
  private String filler;
  @Enumerated(EnumType.STRING)
  private CigarStrength cigarStrength;
  @Enumerated(EnumType.STRING)
  private CigarShape shape;
  @Enumerated(EnumType.STRING)
  private CigarRolling rolling;

  @OneToMany(mappedBy = "cigar", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Image> images;

  @OneToMany(mappedBy = "cigar", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<CigarRate> ratings;

  @ManyToOne
  @JoinColumn(name = "brandId")
  private Brand brand;

  // description
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
  private ZonedDateTime created;

}
