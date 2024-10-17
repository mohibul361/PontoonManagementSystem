package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddPontoonTransferDto;
import com.biwta.pontoon.service.PontoonTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pontoon/transfer")
public class PontoonTransferController {
    private final PontoonTransferService pontoonTransferService;

    @PostMapping("/pontoonTransfer")
    public ResponseEntity<String> addPontoonTransfer(@Valid AddPontoonTransferDto addPontoonTransferDto,
                                                     @RequestParam(required = false) MultipartFile documentsFile,
                                                     HttpServletRequest request) {
        if (pontoonTransferService.addPontoonTransfer(addPontoonTransferDto,documentsFile, request))
            return ResponseEntity.ok("Pontoon Transfer Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Pontoon Transfer Not Saved Successfully Inserted");
    }

    @GetMapping("/pontoonTransferList")
    public ResponseEntity<?> pontoonTransferList() {
        return ResponseEntity.ok(pontoonTransferService.pontoonTransferList());
    }

    @GetMapping("/oldPontoonTransferInfo")
    public ResponseEntity<?> oldPontoonTransferInfo(@RequestParam long pontoonId) {
        return ResponseEntity.ok(pontoonTransferService.oldPontoonTransferInfo(pontoonId));
    }
}
