package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.*;
import com.biwta.pontoon.dto.*;
import com.biwta.pontoon.repository.PontoonTransferRepository;
import com.biwta.pontoon.repository.RouteRepository;
import com.biwta.pontoon.service.*;
import com.biwta.pontoon.utils.FileExtensionUtil;
import com.biwta.pontoon.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ৩০/১১/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PontoonTransferServiceImpl implements PontoonTransferService {
    private final PontoonTransferRepository pontoonTransferRepository;
    private final PontoonService pontoonService;
    private final TransactionService transactionService;
    private final LoshkarService loshkarService;
    private final PontoonSectionService pontoonSectionService;
    private final GhatService ghatService;
    private final PontoonPlacementService pontoonPlacementService;
    private final RouteRepository routeRepository;
    private final Environment ENVIRONMENT;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addPontoonTransfer(AddPontoonTransferDto addPontoonTransferDto, MultipartFile documentsFile, HttpServletRequest request) {
        Pontoon pontoon = pontoonService.getPontoon(addPontoonTransferDto.getPontoonId());
        Ghat ghat = ghatService.getGhat(addPontoonTransferDto.getGhatId());
        PontoonSection pontoonSection = pontoonSectionService.findPontoonDivisionById(addPontoonTransferDto.getSectionId());
        List<PontoonTransfer> existingTransfers = pontoonTransferRepository.findByPontoon_Id(addPontoonTransferDto.getPontoonId());

        if (!existingTransfers.isEmpty()) {
            // Handle multiple existing transfers if needed
            for (PontoonTransfer existingTransferRecord : existingTransfers) {
                // set transferDate - 1 day
                existingTransferRecord.setTillDate(Date.from(addPontoonTransferDto.getTransferDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                pontoonTransferRepository.save(existingTransferRecord);
            }
        }

        // Create a new PontoonTransfer record
        PontoonTransfer pontoonTransfer = new PontoonTransfer();
        pontoonTransfer.setPontoon(pontoon);
        pontoonTransfer.setSection(pontoonSection);
        pontoonTransfer.setGhat(ghat);
        pontoonTransfer.setLatitude(addPontoonTransferDto.getLatitude());
        pontoonTransfer.setLongitude(addPontoonTransferDto.getLongitude());
        pontoonTransfer.setTransferDate(addPontoonTransferDto.getTransferDate());
        pontoonTransfer.setOrderNo(addPontoonTransferDto.getOrderNo());
        pontoonTransfer.setOrderDate(addPontoonTransferDto.getOrderDate());

        // image
        if (documentsFile == null) {
            pontoonTransfer.setDocumentsFile(null);
        } else {
            String uploadDirEmployee = ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonTransferDocuments/";
            String randomNumber = RandomStringUtils.randomNumeric(6);
            // Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isDocumentExtension(documentsFile.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg,jpeg, png,pdf.");
            }
            String mainImageName = StringUtils.cleanPath(documentsFile.getOriginalFilename());
            pontoonTransfer.setDocumentsFile(randomNumber + "_img_" + mainImageName.trim());

            try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + mainImageName.trim(), documentsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Use a List to store multiple employees
        List<Employee> employees = addPontoonTransferDto.getEmployeeId().stream()
                .map(employeeId -> loshkarService.getEmployee(employeeId))
                .collect(Collectors.toList());

        pontoonTransfer.setEmployee(employees);

        // Set the tillDate for the new record (assuming you want to add 3 years)
        pontoonTransfer.setTillDate(Date.from(addPontoonTransferDto.getTransferDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().plusYears(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        PontoonTransfer pt = pontoonTransferRepository.save(pontoonTransfer);
        Transaction t = transactionService.addTransaction(new TransactionDTO("PontoonTransfer added with id: " + pt.getId()), request);

        // update pontoon placement info
       Route route= routeRepository.findByIdAndIsActiveTrue(ghat.getRoute().getId());
       log.info("route id: "+route.getId());
        pontoonPlacementService.updatePontoonPlacement(pontoon.getId(),
                new AddPontoonPlacementDTO(pontoon.getId(),
                        ghat.getId(),
                        route.getId(),addPontoonTransferDto.getLatitude(),addPontoonTransferDto.getLongitude()), request);
        return true;
    }

    @Override
    public List<PontoonTransferList> pontoonTransferList() {
        return pontoonTransferRepository.findAllByOrderByIdDesc().stream().map(pontoonTransfer -> {
            PontoonTransferList pontoonTransferList = new PontoonTransferList();
            pontoonTransferList.setId(pontoonTransfer.getId());
            pontoonTransferList.setPontoonName(pontoonTransfer.getPontoon().getPontoonId());
            pontoonTransferList.setSectionName(pontoonTransfer.getSection().getSectionName());
            pontoonTransferList.setGhatName(pontoonTransfer.getGhat().getGhatName());
            pontoonTransferList.setTransferDate(pontoonTransfer.getTransferDate().toString());
            pontoonTransferList.setLatitude(pontoonTransfer.getLatitude());
            pontoonTransferList.setLongitude(pontoonTransfer.getLongitude());
            return pontoonTransferList;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OldPontoonTransferInfo> oldPontoonTransferInfo(long pontoonId) {
        List<OldPontoonTransferInfo> oldPontoonTransferInfo = pontoonTransferRepository.oldPontoonTransferInfo(pontoonId);
        if(oldPontoonTransferInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Data Found");
        return oldPontoonTransferInfo;
    }

}
