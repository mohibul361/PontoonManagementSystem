package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddLoshkorTransferDto;
import com.biwta.pontoon.service.LoshkorTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loshkar")
public class LoshkarTransferController {
    private final LoshkorTransferService loshkorTransferService;

    @PostMapping("/loshkarTransfer")
    public ResponseEntity<String> addloshkarTransfer(@Valid @RequestBody AddLoshkorTransferDto addLoshkorTransferDto,
                                                     HttpServletRequest request) {
        if (loshkorTransferService.addLochkorTransfer(addLoshkorTransferDto, request))
            return ResponseEntity.ok("Loshkar Transfer Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Loshkar Transfer Not Successfully Inserted");
    }

    @GetMapping("/loshkarTransferList")
    public ResponseEntity<?> getAllLoshkarTransfer() {
        return ResponseEntity.ok(loshkorTransferService.findAll());
    }

    @GetMapping("/oldLoshkarTransferInfo")
    public ResponseEntity<?> getOldLoshkarTransferInfo(@RequestParam long employeeId) {
        return ResponseEntity.ok(loshkorTransferService.oldLoshkarTransferInfo(employeeId));
    }
}
