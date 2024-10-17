package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonSectionRepository extends JpaRepository<PontoonSection, Long> {
    Boolean existsBySectionName(String sectionName);
    Optional<PontoonSection> findByIdAndIsActiveTrue(Long id);
    List<PontoonSection>findAllByIsActiveTrue();
    List<PontoonSection>findAllByIsActiveTrueAndHasDepartmentTrueAndPontoonDepartment_Id(Long departmentId);
    List<PontoonSection>findAllByIsActiveTrueAndHasDepartmentFalse();
}
