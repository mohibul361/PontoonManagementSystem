package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddPontoonDTO;
import com.biwta.pontoon.service.PontoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pontoon")
public class PontoonController {
    private final PontoonService pontoonService;

    @PostMapping("/addPontoon")
    public ResponseEntity<String> addPontoon(@Valid AddPontoonDTO addPontoonDTO,
                                             @RequestPart MultipartFile[] pontoonImage,
                                             HttpServletRequest request) {
        if (pontoonService.addPontoon(addPontoonDTO, pontoonImage, request))
            return ResponseEntity.ok("Pontoon Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Pontoon Not Saved Successfully Inserted");
    }

    @GetMapping("/getPontoon")
    public ResponseEntity<?> getPontoon(@RequestParam Long pontoonId) {
        return ResponseEntity.ok(pontoonService.getPontoon(pontoonId));
    }

    @GetMapping("/unAssaignPontoonList")
    public ResponseEntity<?> unAssaignPontoonList() {
        return ResponseEntity.ok(pontoonService.findAllPontoon());
    }

    @GetMapping("/findAllPontoonWithPagination")
    public ResponseEntity<?> findAllPontoonWithPagination(@RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonService.findAllPontoonWithPagination(pageNo, pageSize, searchKeyword));
    }

    @PutMapping("/updatePontoon")
    public ResponseEntity<?> updatePontoon(AddPontoonDTO addPontoonDTO,
                                           @RequestParam Long id,
                                           @RequestParam(required = false) MultipartFile pontoonImage,
                                           HttpServletRequest request) {
        if (pontoonService.updatePontoon(id, addPontoonDTO, pontoonImage, request)) {
            return ResponseEntity.ok("Pontoon Updated Successfully");
        } else {
            return ResponseEntity.badRequest().body("Pontoon Not Updated Successfully");
        }
    }

    @DeleteMapping("/deletePontoon")
    public ResponseEntity<?> deletePontoon(@RequestParam Long pontoonId,
                                           HttpServletRequest request) {
        if (pontoonService.deletePontoon(pontoonId, request)) {
            return ResponseEntity.ok("Pontoon Deleted Successfully");
        } else {
            return ResponseEntity.badRequest().body("Pontoon Not Deleted Successfully");
        }
    }

    @GetMapping("/assaignPontoonList")
    public ResponseEntity<?> assaignPontoonList() {
        return ResponseEntity.ok(pontoonService.assaignPontoonList());
    }

    @GetMapping("/findAllPontoonList")
    public ResponseEntity<?> findAllPontoonList() {
        return ResponseEntity.ok(pontoonService.findAllPontoonList());
    }


}
