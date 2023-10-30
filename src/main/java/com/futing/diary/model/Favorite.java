package com.futing.diary.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "favorites")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "favoriteId")
  private Integer favoriteId;
  private Integer userId;
  private Integer cigarId;
  private ZonedDateTime created;
}
