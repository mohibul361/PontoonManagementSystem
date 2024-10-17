package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.PontoonStatusUpdateDto;
import com.biwta.pontoon.service.PontoonStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pontoonStatus")
public class PontoonStatusController {
    private final PontoonStatusService pontoonStatusService;

    @PostMapping("/addPontoonStatus")
    public ResponseEntity<?> addPontoonStatus(@RequestParam String statusName) {
        if (pontoonStatusService.addPontoonStatus(statusName) != null)
            return ResponseEntity.ok("PontoonStatus Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonStatus Not Successfully Inserted");
    }

    @PostMapping("/addPontoonStatusUpdate")
    public ResponseEntity<?> addPontoonStatusUpdate(@Valid PontoonStatusUpdateDto pontoonStatusUpdateDto,
                                                    @RequestParam(required = false) MultipartFile pontoonStatusDocument,
                                                    HttpServletRequest request) {
        if (pontoonStatusService.addPontoonStatusUpdate(pontoonStatusUpdateDto,pontoonStatusDocument,request))
            return ResponseEntity.ok("PontoonStatus update Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonStatus update Not Successfully Inserted");
    }

    @GetMapping("/getAllPontoonStatus")
    public ResponseEntity<?> getAllPontoonStatus() {
        return ResponseEntity.ok(pontoonStatusService.getAllPontoonStatus());
    }

    @GetMapping("/getAllPontoonStatusUpdate")
    public ResponseEntity<?> getAllPontoonStatusUpdate() {
        return ResponseEntity.ok(pontoonStatusService.getAllPontoonStatusUpdate());
    }
}
