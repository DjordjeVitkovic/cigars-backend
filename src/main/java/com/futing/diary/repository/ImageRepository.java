package com.futing.diary.repository;

import com.futing.diary.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Integer> {
}
