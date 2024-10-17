package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.PontoonSize;
import com.biwta.pontoon.domain.PontoonType;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.AddPontoonTypeDTO;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PontoonSizeRepository;
import com.biwta.pontoon.repository.PontoonTypeRepository;
import com.biwta.pontoon.service.PontoonTypeService;
import com.biwta.pontoon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class PontoonTypeServiceImpl implements PontoonTypeService {
    private final PontoonSizeRepository pontoonSizeRepository;
    private final TransactionService transactionService;
    private final PontoonTypeRepository pontoonTypeRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addPontoonType(AddPontoonTypeDTO addPontoonTypeDTO, HttpServletRequest request) {
        PontoonSize pontoonSize = new PontoonSize();
        if (isPontoonTypeExists(addPontoonTypeDTO.getTypeName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PontoonType already exists with name: " + addPontoonTypeDTO.getTypeName() + "");
        }
        pontoonSize.setSizeName(addPontoonTypeDTO.getTypeName());
        pontoonSize.setLength(addPontoonTypeDTO.getLength());
        pontoonSize.setBreadth(addPontoonTypeDTO.getBreadth());
        pontoonSize.setHeight(addPontoonTypeDTO.getHeight());
        pontoonSize.setPontoonUnit(addPontoonTypeDTO.getPontoonUnit());
        pontoonSize.setIsActive(true);
        pontoonSize.setPontoonType(pontoonTypeRepository.findById(addPontoonTypeDTO.getPontoonTypeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PontoonType not found with id: " + addPontoonTypeDTO.getPontoonTypeId() + "")));
        PontoonSize pt = pontoonSizeRepository.save(pontoonSize);
        Transaction t = transactionService.addTransaction(new TransactionDTO("PontoonType added with name: " + addPontoonTypeDTO.getTypeName()), request);
        if (pt != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PontoonSize getPontoonType(Long id) {
        return pontoonSizeRepository.findByIsActiveTrueAndId(id)
                .orElseThrow(() -> new IllegalArgumentException("PontoonType not found with id: " + id + ""));

    }

    @Override
    public Page<PontoonSize> findAllPontoonType(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (searchKeyword==null || searchKeyword.isEmpty()) {
            Page<PontoonSize> pontoonTypes = pontoonSizeRepository.findAllByIsActiveTrueOrderByIdDesc(pageable);
            if (pontoonTypes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
            }
            return pontoonTypes;
        } else {
            Page<PontoonSize> pontoonTypes = pontoonSizeRepository.findAllByKeyword(searchKeyword, pageable);
            if (pontoonTypes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found for the given keyword");
            }
            return pontoonTypes;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updatePontoonType(Long pontoonTypeId, AddPontoonTypeDTO addPontoonTypeDTO, HttpServletRequest request) {
        return pontoonSizeRepository.findById(pontoonTypeId)
                .map(pontoonType -> {
                    if (pontoonType.getSizeName() != null) {
                        pontoonType.setSizeName(addPontoonTypeDTO.getTypeName());
                    }
                    if (pontoonType.getLength() != null) {
                        pontoonType.setLength(addPontoonTypeDTO.getLength());
                    }
                    if (pontoonType.getBreadth() != null) {
                        pontoonType.setBreadth(addPontoonTypeDTO.getBreadth());
                    }
                    if (pontoonType.getHeight() != null) {
                        pontoonType.setHeight(addPontoonTypeDTO.getHeight());
                    }
                    if (pontoonType.getPontoonUnit() != null) {
                        pontoonType.setPontoonUnit(addPontoonTypeDTO.getPontoonUnit());
                    }
                    pontoonType.setIsActive(true);
                    PontoonSize pt = pontoonSizeRepository.save(pontoonType);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("PontoonType updated with name: " + addPontoonTypeDTO.getTypeName()), request);
                    if (pt != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PontoonType not found with id: " + pontoonTypeId + ""));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deletePontoonType(Long pontoonTypeId, HttpServletRequest request) {
        return pontoonSizeRepository.findById(pontoonTypeId)
                .map(pontoonType -> {
                    pontoonType.setIsActive(false);
                    PontoonSize pt = pontoonSizeRepository.save(pontoonType);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("PontoonType deleted with name: " + pontoonType.getSizeName()), request);
                    if (pt != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PontoonType not found with id: " + pontoonTypeId + ""));
    }

    @Override
    public List<PontoonSize> findAllPontoonTypeWithoutPagination() {
        List<PontoonSize> pontoonTypes = pontoonSizeRepository.findAllByIsActiveTrueOrderByIdDesc();
        if (pontoonTypes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
        }
        return pontoonTypes;
    }

    @Override
    public List<PontoonType> findAllPontoonType() {
        return pontoonTypeRepository.findAll();
    }

    private Boolean isPontoonTypeExists(String typeName) {
        return pontoonSizeRepository.existsBySizeName(typeName);
    }

}