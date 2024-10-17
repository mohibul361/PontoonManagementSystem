package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Ghat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface GhatRepository extends JpaRepository<Ghat,Long> {
    Boolean existsByGhatName(String ghatName);
    Optional<Ghat> findByIdAndIsActiveTrue(Long id);
    Page<Ghat> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
    @Query("SELECT p FROM Ghat p WHERE " +
            "(:keyword IS NULL OR LOWER(p.ghatName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.section.sectionName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(p.route.routeName) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND p.isActive = true " +
            "ORDER BY p.id DESC")
    Page<Ghat> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
    List<Ghat> findBySectionIdAndIsActiveTrue(Long id);
}
