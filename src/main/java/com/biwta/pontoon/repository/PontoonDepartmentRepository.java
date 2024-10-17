package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonDepartmentRepository extends JpaRepository<PontoonDepartment, Long> {
    Boolean existsByDepartmentName(String departmentName);
    Boolean existsByDepartmentShortCode(String departmentShortCode);
    List<PontoonDepartment>findAllByIsActiveTrueOrderByIdDesc();
    Optional<List<PontoonDepartment>>findAllByDivisionIdAndIsActiveTrue(Long divisionId);

    @Query("SELECT CASE WHEN COUNT(d.division.id) > 0 THEN true ELSE false END FROM PontoonDepartment d WHERE d.division.id = :id AND d.isActive = true")
    Boolean existsByIdAndIsActiveTrue(@Param("id") Long id);

    Optional<PontoonDepartment> findByIdAndIsActiveTrue(Long divisionId);

    List<PontoonDepartment> findAllByOrderByIdDesc();

}
