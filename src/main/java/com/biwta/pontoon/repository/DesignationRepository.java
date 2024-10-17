package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nasimkabir
 * ১৮/১২/২৩
 */
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    List<Designation> findAllByOrderByIdDesc();
}
