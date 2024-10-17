package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Thana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ৪/২/২৪
 */
public interface ThanaRepository extends JpaRepository<Thana, Long> {
    Optional<List<Thana>> findByDistrictId(Long districtId);
}
