package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.PontoonDivision;
import com.biwta.pontoon.dto.AddPontoonDivisionDTO;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PontoonDepartmentRepository;
import com.biwta.pontoon.repository.PontoonDivisionRepository;
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
public class PontoonDivisionServiceImpl implements PontoonDivisionService {
    private final PontoonDivisionRepository pontoonDivisionRepository;
    private final TransactionService transactionService;
    private final PontoonDepartmentRepository pontoonDepartmentService;


    public PontoonDivision addPontoonDivision(AddPontoonDivisionDTO addPontoonDivisionDTO, HttpServletRequest request) {
        PontoonDivision pontoonDivision = new PontoonDivision();
        if (pontoonDivisionRepository.existsByDivisionNameAndIsActive(addPontoonDivisionDTO.getDivisionName(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Division Already Exists");
        }
        pontoonDivision.setDivisionName(addPontoonDivisionDTO.getDivisionName());
        pontoonDivision.setIsActive(true);
        pontoonDivision.setIsAssigned(false);
        transactionService.addTransaction(new TransactionDTO("Add Pontoon Division."), request);
        return pontoonDivisionRepository.save(pontoonDivision);
    }

    public List<PontoonDivision> findAllPontoonDivision() {
        return pontoonDivisionRepository.findAllByOrderByIdDesc();
    }

    @Override
    public PontoonDivision updatePontoonDivision(Long pontoonDivisionId, AddPontoonDivisionDTO addPontoonDivisionDTO, HttpServletRequest request) {
        return pontoonDivisionRepository.findByIdAndIsActiveTrue(pontoonDivisionId).map(pontoonDivision -> {
            if (pontoonDivisionRepository.existsByDivisionNameAndIsActive(addPontoonDivisionDTO.getDivisionName(), true)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Division Already Exists");
            }
            pontoonDivision.setDivisionName(addPontoonDivisionDTO.getDivisionName());
            transactionService.addTransaction(new TransactionDTO("Update Pontoon Division."), request);
            return pontoonDivisionRepository.save(pontoonDivision);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Division Not Found"));
    }

    @Override
    public Boolean deletePontoon(Long divisionId, HttpServletRequest request) {
            return pontoonDivisionRepository.findByIdAndIsActiveTrue(divisionId).map(pontoonDivision -> {
                pontoonDivision.setIsActive(false);
                transactionService.addTransaction(new TransactionDTO("Delete Pontoon Division." + divisionId), request);
                pontoonDivisionRepository.save(pontoonDivision);
                return true;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Division Not Found"));
    }

    public PontoonDivision findPontoonDivisionById(Long id) {
        return pontoonDivisionRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Division Not Found with id: " + id));
    }
}
