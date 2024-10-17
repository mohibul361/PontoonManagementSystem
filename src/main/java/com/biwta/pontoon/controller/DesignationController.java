package com.biwta.pontoon.controller;

import com.biwta.pontoon.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author nasimkabir
 * ১৮/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/designation")
public class DesignationController {
    private final DesignationService designationService;

    @PostMapping("/addDesignation")
    public ResponseEntity<String> addDesignation(String name, Long departmentId) {
        if (designationService.addDesignation(name, departmentId))
            return ResponseEntity.ok("Designation Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Designation Not Successfully Inserted");
    }
    @GetMapping("/getDesignation")
    public ResponseEntity<?> getDesignation(Long id) {
        return ResponseEntity.ok(designationService.getDesignation(id));
    }
    @GetMapping("/designationList")
    public ResponseEntity<?> getAllDesignation() {
        return ResponseEntity.ok(designationService.getAllDesignation());
    }
    @PutMapping("/updateDesignation")
    public ResponseEntity<?> updateDesignation(Long id, String name, Long departmentId) {
        if (designationService.updateDesignation(id, name, departmentId)) {
            return ResponseEntity.ok("Designation Successfully Updated");
        } else {
            return ResponseEntity.badRequest().body("Designation Not Successfully Updated");
        }
    }
    @DeleteMapping("/deleteDesignation")
    public ResponseEntity<?> deleteDesignation(Long id) {
        if (designationService.deleteDesignation(id)) {
            return ResponseEntity.ok("Designation Successfully Deleted");
        } else {
            return ResponseEntity.badRequest().body("Designation Not Successfully Deleted");
        }
    }

}
