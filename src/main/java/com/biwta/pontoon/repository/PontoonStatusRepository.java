package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
public interface PontoonStatusRepository extends JpaRepository<PontoonStatus, Long> {
    Boolean existsByStatusName(String statusName);

    List<PontoonStatus> findAllByOrderByIdDesc();
}
