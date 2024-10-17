package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByIdAndIsActiveTrue(Long id);

    List<Route> findBySection_IdAndIsActiveTrueOrderByIdDesc(Long sectionId);

    List<Route> findByIsActiveTrueOrderByIdDesc();

    Boolean existsByRouteName(String routeName);
}
