package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonSection;
import com.biwta.pontoon.dto.AddPontoonSectionDTO;
import com.biwta.pontoon.dto.PontoonSectionModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonSectionService {
    Boolean addPontoonSection(AddPontoonSectionDTO pontoonSection, HttpServletRequest request);
    PontoonSection findPontoonDivisionById(Long divisionId);
    PontoonSectionModel findById(Long divisionId);
    List<PontoonSection>findAllPontoonSection();
    List<PontoonSection>findAllPontoonSectionHasDepartment(long departmentId);
    List<PontoonSection>findAllPontoonSectionNoDepartment();
    Boolean updatePontoonSection(long sectonId,AddPontoonSectionDTO pontoonSection, HttpServletRequest request);
    Boolean deletePontoonSection(Long divisionId, HttpServletRequest request);
}
