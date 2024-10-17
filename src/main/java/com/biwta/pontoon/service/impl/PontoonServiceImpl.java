package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.*;
import com.biwta.pontoon.dto.*;
import com.biwta.pontoon.repository.PontoonImageRepository;
import com.biwta.pontoon.repository.PontoonRepository;
import com.biwta.pontoon.repository.PontoonStatusRepository;
import com.biwta.pontoon.repository.PontoonStatusUpdateRepository;
import com.biwta.pontoon.service.PontoonService;
import com.biwta.pontoon.service.ProcuringEntryService;
import com.biwta.pontoon.service.TransactionService;
import com.biwta.pontoon.utils.FileExtensionUtil;
import com.biwta.pontoon.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PontoonServiceImpl implements PontoonService {
    private final PontoonRepository pontoonRepository;
    private final TransactionService transactionService;
    private final Environment ENVIRONMENT;
    private final PontoonStatusUpdateRepository pontoonStatusUpdateRepository;
    private final PontoonStatusRepository pontoonStatusRepository;
    private final ProcuringEntryService procuringEntryService;
    private final PontoonImageRepository pontoonImageRepository;

    @Transactional
    @Override
    public Boolean addPontoon(AddPontoonDTO addPontoonDTO, MultipartFile[] pontoonImageArray, HttpServletRequest request) {
        List<MultipartFile> pontoonImageList = Arrays.asList(pontoonImageArray);
        // Now you can use pontoonImageList in the same way as if it were passed directly
        String randomNumber = RandomStringUtils.randomNumeric(6);
        Pontoon pontoon = new Pontoon();
        pontoon.setPontoonSize(addPontoonDTO.getPontoonSize());
        if (pontoonRepository.existsByPontoonId(addPontoonDTO.getPontoonId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Pontoon already exists with id: " + addPontoonDTO.getPontoonId());
        }
        pontoon.setPontoonId(addPontoonDTO.getPontoonId());
        pontoon.setBuildYear(addPontoonDTO.getBuildYear());
        pontoon.setManufacturedBy(addPontoonDTO.getManufacturedBy());
        pontoon.setBuildCost(addPontoonDTO.getBuildCost());
        pontoon.setProcuringEntity(procuringEntryService.getProcuringEntryById(addPontoonDTO.getProcuringEntityId()));
        pontoon.setBudgetSource(addPontoonDTO.getBudgetSource());
        pontoon.setRemarks(addPontoonDTO.getRemarks());
        if (pontoonImageList == null || pontoonImageList.isEmpty()) {
            pontoon.setPontoonImage(null);
        } else {
            List<PontoonImage> pontoonImagesList = new ArrayList<>();
            for (int i = 0; i < pontoonImageList.size(); i++) {
                MultipartFile image = pontoonImageList.get(i);
                if (image.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid file type for pontoon image at index " + i + ". Allowed types are jpg, jpeg, png.");
                }
                String pontoonImageName = StringUtils.cleanPath(image.getOriginalFilename());
                if (!FileExtensionUtil.isDocumentExtension(pontoonImageName.trim())) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid file type for pontoon image at index " + i + ". Allowed types are jpg, jpeg, png.");
                }
                PontoonImage pontoonImage = new PontoonImage();
                pontoonImage.setPontoon(pontoon);
                pontoonImage.setImageUrl(randomNumber + "_" + pontoonImageName.trim());
                pontoonImageRepository.save(pontoonImage);
                pontoonImagesList.add(pontoonImage);
                String uploadDir = ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonImage/";
                try {
                    FileUploadUtil.saveFile(uploadDir, randomNumber + "_" + pontoonImageName.trim(), image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pontoon.setPontoonImage(pontoonImagesList);
        }
        pontoon.setIsActive(true);
        pontoon.setIsAssigned(false);
        pontoon.setReceivingDate(addPontoonDTO.getReceivingDate());
        Pontoon p = pontoonRepository.save(pontoon);

        // Update pontoon images with pontoon ID
        if (pontoonImageList != null) {
            for (PontoonImage pontoonImage : p.getPontoonImage()) {
                pontoonImage.setPontoon(p);
                pontoonImageRepository.save(pontoonImage);
            }
        }
        Transaction t = transactionService.addTransaction(new TransactionDTO("Pontoon added with name: " + addPontoonDTO.getPontoonId()), request);
        return p != null && t != null;
    }

    @Override
   /* public Pontoon getPontoon(Long id) {
        return pontoonRepository.findByIsActiveTrueAndId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pontoon not found with id: " + id));
    }*/
    public Pontoon getPontoon(Long id) {
        Pontoon pontoon = pontoonRepository.findByIsActiveTrueAndId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pontoon not found with id: " + id));

        if (pontoon.getPontoonImage() == null) {
            // Handle the case where pontoonImage is null (initialize it, throw an exception, etc.)
            // For example, you can throw an exception:
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Pontoon Image is null for Pontoon with id: " + id);
        }

        return pontoon;
    }


    @Override
    public List<Pontoon> getAllPontoon(List<Long> pontoonIdList) {
        return pontoonRepository.findAllByIdIn(pontoonIdList);
    }

    @Override
    public List<UnAssignPontoonList> findAllPontoon() {
        List<Pontoon> pontoon = pontoonRepository.findAllByIsAssignedFalseAndIsActiveTrueOrderByIdDesc();
        if (pontoon.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data found for the given keyword");
        }
        return pontoon.stream().map(
                pontoon1 -> {
                    log.info("Pontoon--------------------------: {}", pontoon1.getBudgetSource());
                    UnAssignPontoonList unAssignPontoonList = new UnAssignPontoonList();
                    unAssignPontoonList.setId(pontoon1.getId());
                    unAssignPontoonList.setPontoonType(pontoon1.getPontoonSize());
                    unAssignPontoonList.setPontoonId(pontoon1.getPontoonId());
                    unAssignPontoonList.setBuildYear(pontoon1.getBuildYear());
                    unAssignPontoonList.setManufacturedBy(pontoon1.getManufacturedBy());
                    unAssignPontoonList.setBuildCost(pontoon1.getBuildCost());
                    unAssignPontoonList.setProcuringEntity(pontoon1.getProcuringEntity());
                    unAssignPontoonList.setBudgetSource(pontoon1.getBudgetSource());
                    unAssignPontoonList.setRemarks(pontoon1.getRemarks());
                    unAssignPontoonList.setIsAssigned(pontoon1.getIsAssigned());
                    unAssignPontoonList.setIsActive(pontoon1.getIsActive());
                    unAssignPontoonList.setReceivingDate(pontoon1.getReceivingDate());

                    // Check if pontoonImage is not null
                    if (pontoon1.getPontoonImage() != null) {
                        pontoon1.getPontoonImage().forEach(pontoonImage -> {
                            PontoonImageModel pontoonImageModel = new PontoonImageModel();
                            pontoonImageModel.setId(pontoonImage.getId());
                            pontoonImageModel.setPontoonId(pontoonImage.getPontoon().getId());
                            pontoonImageModel.setImageUrl(pontoonImage.getImageUrl());
                            unAssignPontoonList.getPontoonImage().add(pontoonImageModel);
                        });
                    }
                    return unAssignPontoonList;
                }
        ).collect(Collectors.toList());
    }


    @Override
    public Page<PontoonListProjection> findAllPontoonWithPagination(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            Page<PontoonListProjection> ghats = pontoonRepository.findAllPontoonListWithPagination(pageable);
            if (ghats.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
            }
            return ghats;
        } else {
            Page<PontoonListProjection> ghats = pontoonRepository.findAllPontoonListWithPaginationFilter(searchKeyword, pageable);
            if (ghats.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found for the given keyword");
            }
            return ghats;
        }
    }

    @Override
    public List<PontoonList> assaignPontoonList() {
        return pontoonRepository.findAllByIsAssignedTrueAndIsActiveTrueOrderByIdDesc()
                .stream().map(
                        pontoon -> new PontoonList(
                                pontoon.getId(),
                                pontoon.getPontoonSize().getSizeName(),
                                pontoon.getPontoonId(),
                                pontoon.getBuildYear(),
                                pontoon.getManufacturedBy(),
                                pontoon.getBuildCost(),
                                pontoon.getProcuringEntity().getProcuringName(),
                                pontoon.getBudgetSource(),
                                pontoon.getRemarks(),
                                pontoon.getIsAssigned()
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public Boolean updatePontoon(Long pontoonId, AddPontoonDTO addPontoonDTO, MultipartFile pontoonImage, HttpServletRequest request) {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        return pontoonRepository.findByIsActiveTrueAndId(pontoonId)
                .map(pontoon -> {
                    if (addPontoonDTO.getPontoonId() != null) {
                        pontoon.setPontoonId(addPontoonDTO.getPontoonId());
                    }
                    if (addPontoonDTO.getPontoonSize() != null) {
                        pontoon.setPontoonSize(addPontoonDTO.getPontoonSize());
                    }
                    if (addPontoonDTO.getBuildYear() != null) {
                        pontoon.setBuildYear(addPontoonDTO.getBuildYear());
                    }
                    if (addPontoonDTO.getManufacturedBy() != null) {
                        pontoon.setManufacturedBy(addPontoonDTO.getManufacturedBy());
                    }
                    if (addPontoonDTO.getBuildCost() != null) {
                        pontoon.setBuildCost(addPontoonDTO.getBuildCost());
                    }
                    if (addPontoonDTO.getProcuringEntityId() != 0) {
                        pontoon.setProcuringEntity(procuringEntryService.getProcuringEntryById(addPontoonDTO.getProcuringEntityId()));
                    }
                    if (addPontoonDTO.getBudgetSource() != null) {
                        pontoon.setBudgetSource(addPontoonDTO.getBudgetSource());
                    }
                    if (addPontoonDTO.getRemarks() != null) {
                        pontoon.setRemarks(addPontoonDTO.getRemarks());
                    }
                    if (pontoonImage != null) {
                        List<PontoonImage> pontoonImageList = new ArrayList<>();
                        String pontoonImageName = StringUtils.cleanPath(pontoonImage.getOriginalFilename());
                        // Check if the file extension is allowed for employeeImage
                        if (!FileExtensionUtil.isDocumentExtension(pontoonImage.getOriginalFilename())) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
                        }
                        PontoonImage pontoonImages = new PontoonImage();
                        pontoonImages.setPontoon(pontoon);
                        pontoonImages.setImageUrl(randomNumber + "_" + pontoonImageName.trim());
                        pontoonImageRepository.save(pontoonImages);
                        pontoonImageList.add(pontoonImages);
                        pontoon.setPontoonImage(pontoonImageList);
                        String uploadDir = ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonImage/";
                        try {
                            FileUploadUtil.saveFile(uploadDir, randomNumber + "_" + pontoonImageName.trim(), pontoonImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (addPontoonDTO.getReceivingDate() != null) {
                        pontoon.setReceivingDate(addPontoonDTO.getReceivingDate());
                    }
                    Pontoon p = pontoonRepository.save(pontoon);
                    if (pontoonImage != null) {
                        p.getPontoonImage().stream().forEach(pontoonImage1 -> {
                            pontoonImage1.setPontoon(p);
                            pontoonImageRepository.save(pontoonImage1);
                        });
                    }
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Pontoon updated with name: " + addPontoonDTO.getPontoonId()), request);
                    if (p != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon not found with id: " + pontoonId + ""));
    }

    @Override
    public Boolean deletePontoon(Long pontoonId, HttpServletRequest request) {
        return pontoonRepository.findByIsActiveTrueAndId(pontoonId)
                .map(pontoon -> {
                    pontoon.setIsActive(false);
                    Pontoon p = pontoonRepository.save(pontoon);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Pontoon deleted with name: " + pontoon.getPontoonId()), request);
                    if (p != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon not found with id: " + pontoonId + ""));
    }

    @Override
    public void updatePontoonAssaignedStatus(long pontoonId) {
        pontoonRepository.findByIsActiveTrueAndId(pontoonId)
                .map(pontoon -> {
                    pontoon.setIsAssigned(true);
                    Pontoon p = pontoonRepository.save(pontoon);
                    if (p != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon not found with id: " + pontoonId + ""));
    }

    @Override
    public List<PontoonListProjection> findAllPontoonList() {
        return pontoonRepository.findAllPontoonList();
    }


}

