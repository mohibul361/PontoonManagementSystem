package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonSize;
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
public interface PontoonSizeRepository extends JpaRepository<PontoonSize,Long> {
    Boolean existsBySizeName(String typeName);
    Optional<PontoonSize> findByIsActiveTrueAndId(Long id);
    Page<PontoonSize> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
    List<PontoonSize> findAllByIsActiveTrueOrderByIdDesc();
    @Query("SELECT p FROM PontoonSize p WHERE " +
            "(:keyword IS NULL OR LOWER(p.height) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.sizeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.pontoonUnit) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND p.isActive = true " +
            "ORDER BY p.id DESC")
    Page<PontoonSize> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
