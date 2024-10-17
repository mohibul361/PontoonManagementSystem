package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonDivisionRepository extends JpaRepository<PontoonDivision, Long> {
    Boolean existsByDivisionNameAndIsActive(String divisionName, Boolean isActive);

    List<PontoonDivision> findByIsActiveTrue();
    List<PontoonDivision> findAllByOrderByIdDesc();

    Optional<PontoonDivision> findByIdAndIsActiveTrue(Long id);
}
