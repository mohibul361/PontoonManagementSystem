package com.biwta.pontoon.controller;

import com.biwta.pontoon.service.PontoonSectionService;
import com.biwta.pontoon.dto.AddPontoonSectionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pontoon-section")
public class PontoonSectionController {
    private final PontoonSectionService pontoonSectionService;

    @PostMapping("/addPontoonSection")
    public ResponseEntity<?> addPontoonSection(@Valid @RequestBody AddPontoonSectionDTO addPontoonSectionDTO,
                                               HttpServletRequest request) {
        if (pontoonSectionService.addPontoonSection(addPontoonSectionDTO, request))
            return ResponseEntity.ok("PontoonSection Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonSection Not Saved Successfully Inserted");
    }

    @GetMapping("/getPontoonSection")
    public ResponseEntity<?> getPontoonSection(@RequestParam long pontoonSectionId) {
        return ResponseEntity.ok(pontoonSectionService.findById(pontoonSectionId));
    }
    @GetMapping("/getAllPontoonSection")
    public ResponseEntity<?> getAllPontoonSection() {
        return ResponseEntity.ok(pontoonSectionService.findAllPontoonSection());
    }

    @GetMapping("/getAllPontoonSectionHasDepartment")
    public ResponseEntity<?> findAllPontoonSectionHasDepartment(@RequestParam long departmentId) {
        return ResponseEntity.ok(pontoonSectionService.findAllPontoonSectionHasDepartment(departmentId));
    }

    @GetMapping("/getAllPontoonSectionNoDepartment")
    public ResponseEntity<?> getAllPontoonSectionNoDepartment() {
        return ResponseEntity.ok(pontoonSectionService.findAllPontoonSectionNoDepartment());
    }

    @PutMapping("/updatePontoonSection")
    public ResponseEntity<?> updatePontoonSection(@Valid @RequestBody AddPontoonSectionDTO addPontoonSectionDTO,
                                                  HttpServletRequest request,
                                                  @RequestParam long pontoonSectionId) {
        if (pontoonSectionService.updatePontoonSection(pontoonSectionId,addPontoonSectionDTO, request))
            return ResponseEntity.ok("PontoonSection Updated Successfully");
        else
            return ResponseEntity.badRequest().body("PontoonSection Not Updated Successfully");
    }

    @DeleteMapping("/deletePontoonSection")
    public ResponseEntity<?> deletePontoonSection(@RequestParam long pontoonSectionId,
                                                  HttpServletRequest request) {
        if (pontoonSectionService.deletePontoonSection(pontoonSectionId, request))
            return ResponseEntity.ok("PontoonSection Deleted Successfully");
        else
            return ResponseEntity.badRequest().body("PontoonSection Not Deleted Successfully");
    }

    @GetMapping("/getPontoonSectionById")
    public ResponseEntity<?> getPontoonSectionById(@RequestParam long pontoonSectionId) {
        return ResponseEntity.ok(pontoonSectionService.findById(pontoonSectionId));
    }
}
