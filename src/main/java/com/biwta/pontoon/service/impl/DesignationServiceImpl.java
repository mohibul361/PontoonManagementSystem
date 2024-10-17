package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Designation;
import com.biwta.pontoon.repository.DesignationRepository;
import com.biwta.pontoon.service.DesignationService;
import com.biwta.pontoon.service.PontoonDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nasimkabir
 * ১৮/১২/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DesignationServiceImpl implements DesignationService {
    private final DesignationRepository designationRepository;
    private final PontoonDepartmentService pontoonDepartmentService;
    @Override
    public Boolean addDesignation(String name, Long departmentId) {
        Designation designation = new Designation();
        designation.setName(name);
        designation.setActive(true);
        designation.setDepartment(pontoonDepartmentService. findPontoonDepartmentById(departmentId));
        designationRepository.save(designation);
        if (designation != null)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateDesignation(Long id, String name, Long departmentId) {
        return designationRepository.findById(id).map(designation -> {
            designation.setName(name);
            designation.setDepartment(pontoonDepartmentService. findPontoonDepartmentById(departmentId));
            designationRepository.save(designation);
            return true;
        }).orElseThrow(() -> new RuntimeException("Designation not found"));
    }

    @Override
    public Boolean deleteDesignation(Long id) {
        return designationRepository.findById(id).map(designation -> {
            designation.setActive(false);
            designationRepository.save(designation);
            return true;
        }).orElseThrow(() -> new RuntimeException("Designation not found"));
    }

    @Override
    public Designation getDesignation(Long id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found with id: " + id));
    }

    @Override
    public List<Designation> getAllDesignation() {
        List<Designation> designationList= designationRepository.findAllByOrderByIdDesc();
        if(designationList.isEmpty()){
            throw new RuntimeException("Designation not found");
        }
        return designationList;
    }
}
