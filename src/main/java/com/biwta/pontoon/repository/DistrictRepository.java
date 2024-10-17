package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAllByDivision_IdAndIsActiveTrueOrderById(long id);
}
