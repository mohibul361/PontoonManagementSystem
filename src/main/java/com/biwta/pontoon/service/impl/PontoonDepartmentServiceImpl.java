package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.PontoonDepartment;
import com.biwta.pontoon.domain.PontoonDivision;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.AddPontoonDepartmentDTO;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.dto.UpdatePontoonDepartmentDTO;
import com.biwta.pontoon.repository.PontoonDepartmentRepository;
import com.biwta.pontoon.repository.PontoonDivisionRepository;
import com.biwta.pontoon.service.PontoonDepartmentService;
import com.biwta.pontoon.service.PontoonDivisionService;
import com.biwta.pontoon.service.TransactionService;
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
public class PontoonDepartmentServiceImpl implements PontoonDepartmentService {
    private final TransactionService transactionService;
    private final PontoonDepartmentRepository pontoonDepartmentRepository;
    private final PontoonDivisionService pontoonDivisionService;
    private final PontoonDivisionRepository pontoonDivisionRepository;

    @Override
    public Boolean addPontoonDepartment(AddPontoonDepartmentDTO pontoonDepartmentDTO, HttpServletRequest request) {
        PontoonDivision pontoonDivision = pontoonDivisionService.findPontoonDivisionById(pontoonDepartmentDTO.getPontoonDivisionId());
        PontoonDepartment pontoonDepartment = new PontoonDepartment();
        if (pontoonDepartmentRepository.existsByDepartmentName(pontoonDepartmentDTO.getDepartmentName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Department Name Already Exists");
        }
        pontoonDepartment.setDepartmentName(pontoonDepartmentDTO.getDepartmentName());
        if (pontoonDepartmentRepository.existsByDepartmentShortCode(pontoonDepartmentDTO.getDepartmentShortCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Department Short Code Already Exists");
        }
        pontoonDepartment.setDepartmentShortCode(pontoonDepartmentDTO.getDepartmentShortCode());
        pontoonDepartment.setDivision(pontoonDivision);
        pontoonDepartment.setIsActive(true);
        pontoonDepartment.setIsAssigned(false);
        PontoonDepartment p = pontoonDepartmentRepository.save(pontoonDepartment);
        // update pontoonDivision
        pontoonDivisionRepository.findByIdAndIsActiveTrue(pontoonDepartmentDTO.getPontoonDivisionId()).map(pontoonDivision1 -> {
            pontoonDivision1.setIsAssigned(true);
            return pontoonDivisionRepository.save(pontoonDivision1);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Division Not Found"));

        Transaction t = transactionService.addTransaction(new TransactionDTO("Add Pontoon Department."), request);
        if (p != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PontoonDepartment findPontoonDivisionById(Long divisionId) {
        return pontoonDepartmentRepository.findById(divisionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Department Not Found with id: " + divisionId + "."));
    }

    @Override
    public List<PontoonDepartment> findAllPontoonDivisionById(Long divisionId) {
        return pontoonDepartmentRepository.findAllByDivisionIdAndIsActiveTrue(divisionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Department Not Found with id: " + divisionId + "."));
    }

    @Override
    public List<PontoonDepartment> findAllPontoonDepartmentAndIsActiveTrue() {
        return pontoonDepartmentRepository.findAllByIsActiveTrueOrderByIdDesc();
    }

    @Override
    public PontoonDepartment updatePontoonDepartment(Long pontoonDepartmentId, UpdatePontoonDepartmentDTO addPontoonDepartmentDTO, HttpServletRequest request) {
        return pontoonDepartmentRepository.findById(pontoonDepartmentId).map(pontoonDepartment -> {
            if(addPontoonDepartmentDTO.getDepartmentName() != null && !addPontoonDepartmentDTO.getDepartmentName().isEmpty()){
                pontoonDepartment.setDepartmentName(addPontoonDepartmentDTO.getDepartmentName());
            }
            if(addPontoonDepartmentDTO.getDepartmentShortCode() != null && !addPontoonDepartmentDTO.getDepartmentShortCode().isEmpty()){
                pontoonDepartment.setDepartmentShortCode(addPontoonDepartmentDTO.getDepartmentShortCode());
            }
            PontoonDepartment p = pontoonDepartmentRepository.save(pontoonDepartment);
            Transaction t = transactionService.addTransaction(new TransactionDTO("Update Pontoon Department."), request);
            if (p != null && t != null) {
                return p;
            } else {
                return null;
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Department Not Found with id: " + pontoonDepartmentId + "."));
    }

    @Override
    public Boolean deletePontoonDepartment(Long pontoonId, HttpServletRequest request) {
        return pontoonDepartmentRepository.findById(pontoonId).map(pontoonDepartment -> {
            pontoonDepartment.setIsActive(false);
            PontoonDepartment p = pontoonDepartmentRepository.save(pontoonDepartment);
            Transaction t = transactionService.addTransaction(new TransactionDTO("Delete Pontoon Department."), request);
            if (p != null && t != null) {
                return true;
            } else {
                return false;
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Department Not Found with id: " + pontoonId + "."));
    }

    @Override
    public PontoonDepartment findPontoonDepartmentById(Long pontoonDepartmentId) {
        return pontoonDepartmentRepository.findById(pontoonDepartmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Department Not Found with id: " + pontoonDepartmentId + "."));
    }

    @Override
    public Boolean existsByIdAndIsActiveTrue(Long id) {
        return pontoonDepartmentRepository.existsByIdAndIsActiveTrue(id);
    }
}
