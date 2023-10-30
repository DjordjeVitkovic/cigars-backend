package com.futing.diary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "brands")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandId;
    private String brandName;
    private String countryOfOrigin;
    private String description;
    private String website;
    private String founder;
    private Integer yearEstablished;
    //private String logoImageUrl;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
    private List<Cigar> cigars;

}
