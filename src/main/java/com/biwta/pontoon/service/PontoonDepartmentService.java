package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonDepartment;
import com.biwta.pontoon.dto.AddPontoonDepartmentDTO;
import com.biwta.pontoon.dto.UpdatePontoonDepartmentDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonDepartmentService {
    Boolean addPontoonDepartment(AddPontoonDepartmentDTO pontoonDepartmentDTO, HttpServletRequest request);
    PontoonDepartment findPontoonDivisionById(Long divisionId);
    List<PontoonDepartment> findAllPontoonDivisionById(Long divisionId);
    List<PontoonDepartment>findAllPontoonDepartmentAndIsActiveTrue();
    PontoonDepartment updatePontoonDepartment(Long pontoonDepartmentId, UpdatePontoonDepartmentDTO addPontoonDepartmentDTO, HttpServletRequest request);
    Boolean deletePontoonDepartment(Long pontoonId, HttpServletRequest request);
    PontoonDepartment findPontoonDepartmentById(Long pontoonDepartmentId);

    Boolean existsByIdAndIsActiveTrue(Long id);
}
