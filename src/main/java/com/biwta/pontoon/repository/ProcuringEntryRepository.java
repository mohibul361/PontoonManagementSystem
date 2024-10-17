package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.ProcuringEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
public interface ProcuringEntryRepository extends JpaRepository<ProcuringEntry, Long> {
    Boolean existsByProcuringName(String procuringName);
    List<ProcuringEntry> findAllByIsActiveTrue();
}
