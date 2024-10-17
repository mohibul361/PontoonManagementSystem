package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Pontoon;
import com.biwta.pontoon.domain.PontoonStatus;
import com.biwta.pontoon.domain.PontoonStatusUpdate;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.PontoonStatusUpdateDto;
import com.biwta.pontoon.dto.PontoonStatusUpdateList;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PontoonStatusRepository;
import com.biwta.pontoon.repository.PontoonStatusUpdateRepository;
import com.biwta.pontoon.service.PontoonService;
import com.biwta.pontoon.service.PontoonStatusService;
import com.biwta.pontoon.service.TransactionService;
import com.biwta.pontoon.utils.FileExtensionUtil;
import com.biwta.pontoon.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PontoonStatusServiceImpl implements PontoonStatusService {
    private final PontoonStatusRepository pontoonStatusRepository;
    private final TransactionService transactionService;
    private final Environment ENVIRONMENT;
    private final PontoonService pontoonService;
    private final PontoonStatusUpdateRepository pontoonStatusUpdateRepository;

    @Override
    public PontoonStatus addPontoonStatus(String statusName) {
        PontoonStatus pontoonStatus = new PontoonStatus();
        if (pontoonStatusRepository.existsByStatusName(statusName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status Already Exists");
        }
        pontoonStatus.setStatusName(statusName);
        pontoonStatus.setIsActive(true);
        return pontoonStatusRepository.save(pontoonStatus);
    }

    @Override
    public Boolean addPontoonStatusUpdate(PontoonStatusUpdateDto pontoonStatusUpdateDto, MultipartFile pontoonStatusDocument, HttpServletRequest request) {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        Pontoon pontoon = pontoonService.getPontoon(pontoonStatusUpdateDto.getPontoonId());
        PontoonStatus status = pontoonStatusRepository.findById(pontoonStatusUpdateDto.getStatusId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Status not found with id is " + pontoonStatusUpdateDto.getStatusId()));
        PontoonStatusUpdate pontoonStatusUpdate = new PontoonStatusUpdate();
        pontoonStatusUpdate.setPontoon(pontoon);
        pontoonStatusUpdate.setStatus(status);
        if (pontoonStatusDocument==null) {
            pontoonStatusUpdate.setDocumentUrl(null);
        } else {
            String docFile = StringUtils.cleanPath(pontoonStatusDocument.getOriginalFilename());
            pontoonStatusUpdate.setDocumentUrl(randomNumber + "_" + docFile.trim());
            // Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isImageExtension(pontoonStatusDocument.getOriginalFilename())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            String uploadDir = ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonStatusDocument/";
            try {
                FileUploadUtil.saveFile(uploadDir, randomNumber + "_" + docFile.trim(), pontoonStatusDocument);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pontoonStatusUpdate.setDescription(pontoonStatusUpdateDto.getDescription());
        PontoonStatusUpdate p = pontoonStatusUpdateRepository.save(pontoonStatusUpdate);

        Transaction t = transactionService.addTransaction(new TransactionDTO("Pontoon status update added with name: " + pontoonStatusUpdateDto.getPontoonId()), request);
        if (p != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<PontoonStatus> getAllPontoonStatus() {
        return pontoonStatusRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<PontoonStatusUpdateList> getAllPontoonStatusUpdate() {
        return pontoonStatusUpdateRepository.findAllByOrderByIdDesc()
                .stream()
                .map(pontoonStatusUpdate -> new PontoonStatusUpdateList(pontoonStatusUpdate.getId(), pontoonStatusUpdate.getPontoon().getPontoonId(),
                        pontoonStatusUpdate.getStatus().getStatusName(), pontoonStatusUpdate.getDescription()))
                .collect(Collectors.toList());
    }
}
