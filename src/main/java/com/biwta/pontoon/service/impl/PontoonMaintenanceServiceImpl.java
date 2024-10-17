package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Company;
import com.biwta.pontoon.domain.Pontoon;
import com.biwta.pontoon.domain.PontoonMaintenance;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.MaitenanceListProjection;
import com.biwta.pontoon.dto.PontoonMaintenanceDto;
import com.biwta.pontoon.dto.PontoonMaintenanceList;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.enumuration.MaintenanceType;
import com.biwta.pontoon.repository.PontoonMaintenanceRepository;
import com.biwta.pontoon.repository.UserRepository;
import com.biwta.pontoon.service.*;
import com.biwta.pontoon.utils.EntityUtils;
import com.biwta.pontoon.utils.FileExtensionUtil;
import com.biwta.pontoon.utils.FileUploadUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Service
@AllArgsConstructor
@Slf4j
public class PontoonMaintenanceServiceImpl implements PontoonMaintenanceService {
    private final PontoonMaintenanceRepository pontoonMaintenanceRepository;
    private final PontoonService pontoonService;
    private final Environment ENVIRONMENT;
    private final CompanyService companyService;
    private final TransactionService transactionService;
    private final PontoonDepartmentService pontoonDepartmentService;
    private final LoshkarService loshkarService;
    private final UserRepository userRepository;
    private final ProcuringEntryService procuringEntryService;


    @Override
    public Boolean addPontoonMaintenance(PontoonMaintenanceDto pontoonMaintenanceDto, MultipartFile pontoonMainImage, MultipartFile ltmDocumentUrl,
                                         MultipartFile approvalDocumentUrl,
                                         HttpServletRequest request) {
        String uploadDirEmployee = ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonMaintenanceDocument/";
        String randomNumber = RandomStringUtils.randomNumeric(6);

        Pontoon pontoon = pontoonService.getPontoon(pontoonMaintenanceDto.getPontoonId());
        Company company = companyService.getCompany(pontoonMaintenanceDto.getRepairedBy());
        PontoonMaintenance pontoonMaintenance = new PontoonMaintenance();
        pontoonMaintenance.setPontoon(pontoon);
        pontoonMaintenance.setMaintenanceType(pontoonMaintenanceDto.getMaintenanceType());
        pontoonMaintenance.setRepairDateRange(pontoonMaintenanceDto.getRepairDateRange());
        pontoonMaintenance.setRepairCost(pontoonMaintenanceDto.getRepairCost());
        pontoonMaintenance.setRepairedBy(company);
        pontoonMaintenance.setRepairDescription(pontoonMaintenanceDto.getRepairDescription());
        pontoonMaintenance.setFinancialYear(pontoonMaintenanceDto.getFinancialYear());
        pontoonMaintenance.setBudgetSource(pontoonMaintenanceDto.getBudgetSource());
        pontoonMaintenance.setProcuringEntity(procuringEntryService.getProcuringEntryById(pontoonMaintenanceDto.getProcuringEntityId()));
        pontoonMaintenance.setTenderingMethod(pontoonMaintenanceDto.getTenderingMethod());
        if (pontoonMaintenanceDto.getRepairCost() > 1000000) {
            pontoonMaintenance.setMaintenanceType(MaintenanceType.Minor);
        } else {
            pontoonMaintenance.setMaintenanceType(MaintenanceType.Major);
        }
        if (pontoonMainImage == null) {
            pontoonMaintenance.setDocumentUrl(null);
        } else {
            // Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isImageExtension(pontoonMainImage.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            String mainImageName = StringUtils.cleanPath(pontoonMainImage.getOriginalFilename());
            pontoonMaintenance.setDocumentUrl(randomNumber + "_img_" + mainImageName.trim());

            try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + mainImageName.trim(), pontoonMainImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (approvalDocumentUrl == null) {
            pontoonMaintenance.setApprovalDocumentUrl(null);
        } else {
            // Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isImageExtension(pontoonMainImage.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            String appMainImageName = StringUtils.cleanPath(approvalDocumentUrl.getOriginalFilename());
            pontoonMaintenance.setApprovalDocumentUrl(randomNumber + "_img_" + appMainImageName.trim());
            try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + appMainImageName.trim(), approvalDocumentUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ltmDocumentUrl == null) {
            pontoonMaintenance.setLtmDocumentUrl(null);
        } else {
            // Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isImageExtension(ltmDocumentUrl.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            String appMainImageName = StringUtils.cleanPath(ltmDocumentUrl.getOriginalFilename());
            pontoonMaintenance.setLtmDocumentUrl(randomNumber + "_img_" + appMainImageName.trim());
            try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + appMainImageName.trim(), ltmDocumentUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pontoonMaintenance.setIsActive(true);
        String currentUser = EntityUtils.getUserName();
        if (pontoonMaintenanceDto.getDepartmentId() == 0) {
            long departmentId = userRepository.findByEmailOrUsernameIgnoreCaseAndActivated(currentUser, true).get().getEmployee().getDepartment().getId();
            pontoonMaintenance.setDepartment(pontoonDepartmentService.findPontoonDepartmentById(departmentId));
        } else {
            pontoonMaintenance.setDepartment(pontoonDepartmentService.findPontoonDepartmentById(pontoonMaintenanceDto.getDepartmentId()));
        }
        PontoonMaintenance pm = pontoonMaintenanceRepository.save(pontoonMaintenance);
        Transaction t = transactionService.addTransaction(new TransactionDTO("pontoon Maintenance inserted id is " + pm.getPontoon().getPontoonId()), request);
        if (pm != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<PontoonMaintenanceList> getAllPontoonMaintenance(int page, int size, String searchKey) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        if (searchKey != null && !searchKey.isEmpty()) {
            return pontoonMaintenanceRepository.findAllByPontoonIOrPontoonNamedAndIsActiveTrue(searchKey, pageable)
                    .map(this::convertToResponseModel);
        }

        return pontoonMaintenanceRepository.findAllByIsActiveTrue(pageable)
                .map(this::convertToResponseModel);
    }

    @Override
    public Boolean updatePontoonMaintenance(long maintenanceId, PontoonMaintenanceDto pontoonMaintenanceDto, MultipartFile pontoonMainImage, MultipartFile ltmDocumentUrl, MultipartFile approvalDocumentUrl, HttpServletRequest request) {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        return this.pontoonMaintenanceRepository.findById(maintenanceId)
                .map(pontoonMaintenance -> {
                    if (pontoonMaintenanceDto.getPontoonId() != 0) {
                        Pontoon pontoon = pontoonService.getPontoon(pontoonMaintenanceDto.getPontoonId());
                        pontoonMaintenance.setPontoon(pontoon);
                    }
                    if (pontoonMaintenanceDto.getMaintenanceType() != null) {
                        pontoonMaintenance.setMaintenanceType(pontoonMaintenanceDto.getMaintenanceType());
                    }
                    if (pontoonMaintenanceDto.getRepairDateRange() != null) {
                        pontoonMaintenance.setRepairDateRange(pontoonMaintenanceDto.getRepairDateRange());
                    }
                    if (pontoonMaintenanceDto.getRepairCost() != null) {
                        pontoonMaintenance.setRepairCost(pontoonMaintenanceDto.getRepairCost());
                    }
                    if (pontoonMaintenanceDto.getRepairedBy() != 0) {
                        Company company = companyService.getCompany(pontoonMaintenanceDto.getRepairedBy());
                        pontoonMaintenance.setRepairedBy(company);
                    }
                    if (pontoonMaintenanceDto.getRepairDescription() != null) {
                        pontoonMaintenance.setRepairDescription(pontoonMaintenanceDto.getRepairDescription());
                    }
                    if (pontoonMaintenanceDto.getFinancialYear() != null) {
                        pontoonMaintenance.setFinancialYear(pontoonMaintenanceDto.getFinancialYear());
                    }
                    if (pontoonMaintenanceDto.getBudgetSource() != null) {
                        pontoonMaintenance.setBudgetSource(pontoonMaintenanceDto.getBudgetSource());
                    }
                    if (pontoonMaintenanceDto.getProcuringEntityId() != 0) {
                        pontoonMaintenance.setProcuringEntity(procuringEntryService.getProcuringEntryById(pontoonMaintenanceDto.getProcuringEntityId()));
                    }
                    if (pontoonMaintenanceDto.getTenderingMethod() != null) {
                        pontoonMaintenance.setTenderingMethod(pontoonMaintenanceDto.getTenderingMethod());
                    }
                    if (pontoonMaintenanceDto.getMaintenanceType() != null) {
                        pontoonMaintenance.setMaintenanceType(pontoonMaintenanceDto.getMaintenanceType());
                    }
                    if (pontoonMainImage == null) {
                        pontoonMaintenance.setDocumentUrl(null);
                    } else {
                        // Check if the file extension is allowed for employeeImage
                        if (!FileExtensionUtil.isImageExtension(pontoonMainImage.getOriginalFilename())) {
                            throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
                        }
                        String mainImageName = StringUtils.cleanPath(pontoonMainImage.getOriginalFilename());
                        pontoonMaintenance.setDocumentUrl(randomNumber + "_img_" + mainImageName.trim());

                        try {
                            FileUploadUtil.saveFile(ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonMaintenanceDocument/", randomNumber + "_img_" + mainImageName.trim(), pontoonMainImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ltmDocumentUrl == null) {
                        pontoonMaintenance.setLtmDocumentUrl(null);
                    } else {
                        // Check if the file extension is allowed for employeeImage
                        if (!FileExtensionUtil.isImageExtension(ltmDocumentUrl.getOriginalFilename())) {
                            throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
                        }
                        String itemMainImageName = StringUtils.cleanPath(ltmDocumentUrl.getOriginalFilename());
                        pontoonMaintenance.setLtmDocumentUrl(randomNumber + "_img_" + itemMainImageName.trim());
                        try {
                            FileUploadUtil.saveFile(ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonMaintenanceDocument/", randomNumber + "_img_" + itemMainImageName.trim(), ltmDocumentUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (approvalDocumentUrl == null) {
                        pontoonMaintenance.setApprovalDocumentUrl(null);
                    } else {
                        // Check if the file extension is allowed for employeeImage
                        if (!FileExtensionUtil.isImageExtension(pontoonMainImage.getOriginalFilename())) {
                            throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
                        }
                        String appMainImageName = StringUtils.cleanPath(approvalDocumentUrl.getOriginalFilename());
                        pontoonMaintenance.setApprovalDocumentUrl(randomNumber + "_img_" + appMainImageName.trim());
                        try {
                            FileUploadUtil.saveFile(ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonMaintenanceDocument/", randomNumber + "_img_" + appMainImageName.trim(), approvalDocumentUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    pontoonMaintenance.setIsActive(true);
                    PontoonMaintenance pm = pontoonMaintenanceRepository.save(pontoonMaintenance);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("pontoon Maintenance inserted id is " + pm.getPontoon().getPontoonId()), request);
                    if (pm != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Maintenance not found with id " + maintenanceId + "."));
    }

    @Override
    public PontoonMaintenanceList getPontoonMaintenanceById(Long pontoonMaintenanceId) {
        return pontoonMaintenanceRepository.findById(pontoonMaintenanceId)
                .map(this::convertToResponseModel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Maintenance not found with id " + pontoonMaintenanceId + "."));
    }

    @Override
    public List<MaitenanceListProjection> findAllMaintenanceList() {
        List<MaitenanceListProjection> allMaintenanceList = pontoonMaintenanceRepository.findAllMaintenanceList();
        if (allMaintenanceList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        return allMaintenanceList;
    }

    private PontoonMaintenanceList convertToResponseModel(PontoonMaintenance pontoonMaintenance) {
        PontoonMaintenanceList pontoonMaintenanceList = new PontoonMaintenanceList();
        pontoonMaintenanceList.setId(pontoonMaintenance.getId());
        pontoonMaintenanceList.setPontoonId(pontoonMaintenance.getPontoon().getId());
        pontoonMaintenanceList.setPontoonName(pontoonMaintenance.getPontoon().getPontoonId());
        pontoonMaintenanceList.setMaintenanceType(pontoonMaintenance.getMaintenanceType().name());
        pontoonMaintenanceList.setRepairDateRange(pontoonMaintenance.getRepairDateRange().toString());
        pontoonMaintenanceList.setRepairCost(pontoonMaintenance.getRepairCost());
        pontoonMaintenanceList.setRepairedBy(pontoonMaintenance.getRepairedBy().getCompanyName());
        pontoonMaintenanceList.setCompanyId(pontoonMaintenance.getRepairedBy().getId());
        pontoonMaintenanceList.setRepairDescription(pontoonMaintenance.getRepairDescription());
        pontoonMaintenanceList.setFinancialYear(pontoonMaintenance.getFinancialYear());
        pontoonMaintenanceList.setBudgetSource(pontoonMaintenance.getBudgetSource().name());
        pontoonMaintenanceList.setProcuringEntity(pontoonMaintenance.getProcuringEntity().getProcuringName());
        pontoonMaintenanceList.setTenderingMethod(pontoonMaintenance.getTenderingMethod().name());
        pontoonMaintenanceList.setDepartmentName(pontoonMaintenance.getDepartment().getDepartmentName());
        return pontoonMaintenanceList;
    }
}
