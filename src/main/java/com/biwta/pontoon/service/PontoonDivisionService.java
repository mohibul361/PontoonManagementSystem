package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonDivision;
import com.biwta.pontoon.dto.AddPontoonDivisionDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonDivisionService {
    PontoonDivision addPontoonDivision(AddPontoonDivisionDTO pontoonDivisionDTO, HttpServletRequest request);

   PontoonDivision findPontoonDivisionById(Long divisionId);

    List<PontoonDivision> findAllPontoonDivision();

    PontoonDivision updatePontoonDivision(Long pontoonDivisionId, AddPontoonDivisionDTO addPontoonDivisionDTO, HttpServletRequest request);

    Boolean deletePontoon(Long pontoonId, HttpServletRequest request);
}
