package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.MaitenanceListProjection;
import com.biwta.pontoon.dto.PontoonMaintenanceDto;
import com.biwta.pontoon.dto.PontoonMaintenanceList;
import com.biwta.pontoon.service.PontoonMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maintenance")
public class PontoonMaintenanceController {
    private final PontoonMaintenanceService pontoonMaintenanceService;

    @PostMapping("/addPontoonMaintenance")
    public ResponseEntity<String> addPontoonMaintenance(@Valid PontoonMaintenanceDto pontoonMaintenanceDto,
                                                        @RequestParam(required = false) MultipartFile pontoonMainImage,
                                                        @RequestParam(required = false) MultipartFile ltmDocumentUrl,
                                                        @RequestParam(required = false) MultipartFile approvalDocumentUrl,
                                                        HttpServletRequest request
    ) {
        if (pontoonMaintenanceService.addPontoonMaintenance(pontoonMaintenanceDto, pontoonMainImage,ltmDocumentUrl, approvalDocumentUrl, request))
            return ResponseEntity.ok("PontoonMaintenance Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonMaintenance Successfully Inserted");
    }

    @GetMapping("/getAllPontoonMaintenance")
    public ResponseEntity<Page<PontoonMaintenanceList>> getAllPontoonMaintenance(@RequestParam(defaultValue = "0") int pageNo,
                                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                                 @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonMaintenanceService.getAllPontoonMaintenance(pageNo, pageSize, searchKeyword));
    }

    @PutMapping("/updatePontoonMaintenance")
    public ResponseEntity<String> updatePontoonMaintenance(@RequestParam long pontoonMaintenanceId,
                                                           @Valid PontoonMaintenanceDto pontoonMaintenanceDto,
                                                           @RequestParam(required = false) MultipartFile pontoonMainImage,
                                                           @RequestParam(required = false) MultipartFile ltmDocumentUrl,
                                                           @RequestParam(required = false) MultipartFile approvalDocumentUrl,
                                                           HttpServletRequest request) {
        if (pontoonMaintenanceService.updatePontoonMaintenance(pontoonMaintenanceId, pontoonMaintenanceDto, pontoonMainImage, ltmDocumentUrl, approvalDocumentUrl, request))
            return ResponseEntity.ok("PontoonMaintenance Successfully Updated");
        else
            return ResponseEntity.badRequest().body("PontoonMaintenance Successfully Updated");
    }

    @GetMapping("/getPontoonMaintenanceById")
    public ResponseEntity<PontoonMaintenanceList> getPontoonMaintenanceById(@RequestParam Long pontoonMaintenanceId) {
        return ResponseEntity.ok(pontoonMaintenanceService.getPontoonMaintenanceById(pontoonMaintenanceId));
    }

    @GetMapping("/getAllMaintenanceList")
    public ResponseEntity<List<MaitenanceListProjection>> getAllMaintenanceList() {
        return ResponseEntity.ok(pontoonMaintenanceService.findAllMaintenanceList());
    }
}
