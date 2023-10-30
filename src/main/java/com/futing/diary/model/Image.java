package com.futing.diary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@Builder
@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "imageId")
  private Integer imageId;
  private String link;
  private String publicId;
  private ZonedDateTime created;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cigarId")
  @JsonIgnore
  private Cigar cigar;

}
