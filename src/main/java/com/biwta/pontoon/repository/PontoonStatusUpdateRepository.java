package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonStatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
public interface PontoonStatusUpdateRepository extends JpaRepository<PontoonStatusUpdate, Long> {
    List<PontoonStatusUpdate> findAllByOrderByIdDesc();
}
