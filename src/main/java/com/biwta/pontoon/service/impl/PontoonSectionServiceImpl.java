package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.PontoonDepartment;
import com.biwta.pontoon.domain.PontoonDivision;
import com.biwta.pontoon.domain.PontoonSection;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.AddPontoonSectionDTO;
import com.biwta.pontoon.dto.PontoonSectionModel;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PontoonDepartmentRepository;
import com.biwta.pontoon.repository.PontoonSectionRepository;
import com.biwta.pontoon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PontoonSectionServiceImpl implements PontoonSectionService {
    private final PontoonSectionRepository pontoonSectionRepository;
    private final PontoonDivisionService pontoonDivisionService;
    private final PontoonDepartmentService pontoonDepartmentService;
    private final TransactionService transactionService;
    private final PontoonDepartmentRepository pontoonDepartmentRepository;
    private final LocationService locationService;

    @Override
    public Boolean addPontoonSection(AddPontoonSectionDTO pontoonSectionDTO, HttpServletRequest request) {
        PontoonDivision division = pontoonDivisionService.findPontoonDivisionById(pontoonSectionDTO.getDivisionId());
        if (pontoonSectionRepository.existsBySectionName(pontoonSectionDTO.getSectionName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Section Name Already Exists");
        }
        PontoonSection pontoonSection = new PontoonSection();
        pontoonSection.setSectionName(pontoonSectionDTO.getSectionName());
        pontoonSection.setPontoonDivision(division);
        pontoonSection.setHasDepartment(pontoonSectionDTO.getHasDepartment());
        if (pontoonSectionDTO.getHasDepartment().equals(true)) {
            PontoonDepartment department = pontoonDepartmentService.findPontoonDivisionById(pontoonSectionDTO.getDepartmentId());
            pontoonSection.setPontoonDepartment(department);
        } else {
            pontoonSection.setPontoonDepartment(null);
        }
        pontoonSection.setIsActive(true);
        pontoonSection.setIsAssigned(false);
        pontoonSection.setDistrict(locationService.getDistrictById(pontoonSectionDTO.getDistrict_Id()));
        pontoonSection.setDivision(locationService.getDivisionById(pontoonSectionDTO.getDivisionId()));
        PontoonSection p = pontoonSectionRepository.save(pontoonSection);
        // update division and section
        if (pontoonSectionDTO.getHasDepartment().equals(true)) {
            pontoonDepartmentRepository.findByIdAndIsActiveTrue(pontoonSectionDTO.getDepartmentId()).map(pontoonDivision1 -> {
                pontoonDivision1.setIsAssigned(true);
                return pontoonDepartmentRepository.save(pontoonDivision1);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Division Not Found with id: " + pontoonSectionDTO.getDepartmentId() + ". Because it is not active."));
        }
        Transaction t = transactionService.addTransaction(new TransactionDTO("Add Pontoon Section."), request);
        if (p != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PontoonSection findPontoonDivisionById(Long divisionId) {
        return pontoonSectionRepository.findByIdAndIsActiveTrue(divisionId)
                .orElseThrow(() -> new RuntimeException("Pontoon Section not found with id: " + divisionId));
    }

    @Override
    public PontoonSectionModel findById(Long sectionId) {
        return pontoonSectionRepository.findByIdAndIsActiveTrue(sectionId)
                .map(pontoonSection -> {
                    PontoonSectionModel pontoonSectionModel = new PontoonSectionModel();
                    pontoonSectionModel.setId(pontoonSection.getId());
                    pontoonSectionModel.setSectionName(pontoonSection.getSectionName());
                    pontoonSectionModel.setIsActive(pontoonSection.getIsActive());
                    pontoonSectionModel.setPontoonDivisionId(pontoonSection.getPontoonDivision().getId());
                    pontoonSectionModel.setPontoonDivisionName(pontoonSection.getPontoonDivision().getDivisionName());
                    pontoonSectionModel.setHasDepartment(pontoonSection.getHasDepartment());
                    pontoonSectionModel.setIsAssigned(pontoonSection.getIsAssigned());
                    pontoonSectionModel.setDivisionId(pontoonSection.getDivision().getId());
                    pontoonSectionModel.setDivisionName(pontoonSection.getDivision().getDivisionName());
                    pontoonSectionModel.setDistrictId(pontoonSection.getDistrict().getId());
                    pontoonSectionModel.setDistrictName(pontoonSection.getDistrict().getDistrictName());
                    pontoonSectionModel.setDepartmentId(pontoonSection.getPontoonDepartment() != null ? pontoonSection.getPontoonDepartment().getId() : 0);
                    pontoonSectionModel.setDepartmentName(pontoonSection.getPontoonDepartment() != null ? pontoonSection.getPontoonDepartment().getDepartmentName() : null);
                    pontoonSectionModel.setThanaId(pontoonSection.getThana().getId());
                    pontoonSectionModel.setThanaName(pontoonSection.getThana().getThanaName());
                    return pontoonSectionModel;
                }).orElseThrow(() -> new RuntimeException("Pontoon Section not found with id: " + sectionId));
    }

    @Override
    public List<PontoonSection> findAllPontoonSection() {
        return pontoonSectionRepository.findAllByIsActiveTrue();
    }

    @Override
    public List<PontoonSection> findAllPontoonSectionHasDepartment(long departmentId) {
        List<PontoonSection> pontoonSectionList = pontoonSectionRepository.findAllByIsActiveTrueAndHasDepartmentTrueAndPontoonDepartment_Id(departmentId);
        /*if(pontoonSectionList.size()>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data found");
        }*/
        return pontoonSectionList;
    }

    @Override
    public List<PontoonSection> findAllPontoonSectionNoDepartment() {
        return pontoonSectionRepository.findAllByIsActiveTrueAndHasDepartmentFalse();
    }

    @Override
    public Boolean updatePontoonSection(long sectonId, AddPontoonSectionDTO pontoonSection, HttpServletRequest request) {
        return pontoonSectionRepository.findByIdAndIsActiveTrue(sectonId)
                .map(pontoonSection1 -> {
                    if (!pontoonSection1.getSectionName().equals(pontoonSection.getSectionName())) {
                        pontoonSection1.setSectionName(pontoonSection.getSectionName());
                    }
                    if (!pontoonSection1.getDivision().equals(pontoonSection.getDivisionId())) {
                        pontoonSection1.setPontoonDivision(pontoonDivisionService.findPontoonDivisionById(pontoonSection.getDivisionId()));
                    }
                    pontoonSection1.setHasDepartment(pontoonSection.getHasDepartment());
                    if (pontoonSection.getHasDepartment().equals(true)) {
                        pontoonSection1.setPontoonDepartment(pontoonDepartmentService.findPontoonDivisionById(pontoonSection.getDepartmentId()));
                    } else {
                        pontoonSection1.setPontoonDepartment(null);
                    }
                    if (pontoonSection1.getDistrict().getId() != pontoonSection.getDistrict_Id()) {
                        pontoonSection1.setDistrict(locationService.getDistrictById(pontoonSection.getDistrict_Id()));
                    }
                    if (pontoonSection1.getDivision().getId() != pontoonSection.getDivision_Id()) {
                        pontoonSection1.setDivision(locationService.getDivisionById(pontoonSection.getDistrict_Id()));
                    }
                    pontoonSection1.setIsActive(true);
                    PontoonSection p = pontoonSectionRepository.save(pontoonSection1);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Update Pontoon Section."), request);
                    if (p != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new RuntimeException("Pontoon Section not found with id: " + sectonId));
    }

    @Override
    public Boolean deletePontoonSection(Long divisionId, HttpServletRequest request) {
        return pontoonSectionRepository.findByIdAndIsActiveTrue(divisionId)
                .map(pontoonSection -> {
                    pontoonSection.setIsActive(false);
                    PontoonSection p = pontoonSectionRepository.save(pontoonSection);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Delete Pontoon Section."), request);
                    if (p != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new RuntimeException("Pontoon Section not found with id: " + divisionId));
    }
}
