package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.UpdatePontoonDepartmentDTO;
import com.biwta.pontoon.service.PontoonDepartmentService;
import com.biwta.pontoon.dto.AddPontoonDepartmentDTO;
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
@RequestMapping("/api/v1/pontoonDepartment")
public class PontoonDepartmentController {
    private final PontoonDepartmentService pontoonDepartmentService;

    @PostMapping("/addPontoonDepartment")
    public ResponseEntity<String> addPontoonDepartment(@Valid @RequestBody AddPontoonDepartmentDTO addPontoonDepartmentDTO,
                                                       HttpServletRequest request) {
        if (pontoonDepartmentService.addPontoonDepartment(addPontoonDepartmentDTO, request))
            return ResponseEntity.ok("PontoonDepartment Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonDepartment Not Saved Successfully Inserted");
    }

    @GetMapping("/getPontoonDepartment")
    public ResponseEntity<?> getPontoonDepartment(@RequestParam long pontoonDepartmentId) {
        return ResponseEntity.ok(pontoonDepartmentService.findPontoonDivisionById(pontoonDepartmentId));
    }

    @GetMapping("/findAllPontoonDivisionById")
    public ResponseEntity<?> findAllPontoonDivisionById(@RequestParam long pontoonDepartmentId) {
        return ResponseEntity.ok(pontoonDepartmentService.findAllPontoonDivisionById(pontoonDepartmentId));
    }
    @GetMapping("/getAllPontoonDepartment")
    public ResponseEntity<?> getAllPontoonDepartment() {
        return ResponseEntity.ok(pontoonDepartmentService.findAllPontoonDepartmentAndIsActiveTrue());
    }

    @PutMapping("/updatePontoonDepartment")
    public ResponseEntity<?> updatePontoonDepartment(@RequestParam long pontoonDepartmentId,
                                                     @Valid @RequestBody UpdatePontoonDepartmentDTO addPontoonDepartmentDTO,
                                                     HttpServletRequest request) {
        return ResponseEntity.ok(pontoonDepartmentService.updatePontoonDepartment(pontoonDepartmentId, addPontoonDepartmentDTO, request));
    }

    @DeleteMapping("/deletePontoonDepartment")
    public ResponseEntity<?> deletePontoonDepartment(@RequestParam long pontoonDepartmentId,
                                                     HttpServletRequest request) {
        if (pontoonDepartmentService.deletePontoonDepartment(pontoonDepartmentId, request))
            return ResponseEntity.ok("PontoonDepartment Deleted Successfully");
        else
            return ResponseEntity.badRequest().body("PontoonDepartment Not Deleted Successfully");
    }

    @GetMapping("/getPontoonDepartmentByDivisionId")
    public ResponseEntity<?> getPontoonDepartmentByDivisionId(@RequestParam long divisionId) {
        return ResponseEntity.ok(pontoonDepartmentService.findAllPontoonDivisionById(divisionId));
    }
}
