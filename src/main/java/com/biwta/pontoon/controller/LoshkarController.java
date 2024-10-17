package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddEmployeeDTO;
import com.biwta.pontoon.dto.UpdateEmployeeDTO;
import com.biwta.pontoon.service.LoshkarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/loshkar")
public class LoshkarController {
    private final LoshkarService loshkarService;

    @PostMapping("/addLoshkar")
    public ResponseEntity<String> addLoshkar(@Valid AddEmployeeDTO addLoshkarDTO,
                                             @RequestParam(required = false) MultipartFile employeeImage,
                                             @RequestParam(required = false) MultipartFile employeeSignature,
                                             HttpServletRequest request) {
        if (loshkarService.addEmployee(addLoshkarDTO, employeeImage, employeeSignature, request))
            return ResponseEntity.ok("Loshkar Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Loshkar Not Successfully Inserted");
    }

    @GetMapping("/getLoshkar")
    public ResponseEntity<?> getLoshkar(@RequestParam Long loshkarId) {
        return ResponseEntity.ok(loshkarService.getEmployee(loshkarId));
    }

    @PostMapping("/getLoshkars")
    public ResponseEntity<?> getLoshkars(@RequestBody List<Long> loshkarId) {
        return ResponseEntity.ok(loshkarService.getEmployees(loshkarId));
    }

    @GetMapping("/loshkarList")
    public ResponseEntity<?> getAllLoshkar(@RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "10") int pageSize,
                                           @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(loshkarService.findAllEmployee(pageNo, pageSize, searchKeyword));
    }

    @GetMapping("/loshkarListWithAdmin")
    public ResponseEntity<?> loshkarListWithAdmin(@RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(loshkarService.loshkarListWithAdmin(pageNo, pageSize, searchKeyword));
    }

    @GetMapping("/loshkarListWithoutPagination")
    public ResponseEntity<?> loshkarListWithoutPagination() {
        return ResponseEntity.ok(loshkarService.loshkarListWithoutPagination());
    }

    @GetMapping("/loshkarListWithPagination")
    public ResponseEntity<?> loshkarListWithPagination(@RequestParam(defaultValue = "0") int pageNo,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(loshkarService.loshkarListWithPagination(pageNo, pageSize));
    }
    @PutMapping("/updateLoshkar")
    public ResponseEntity<?> updateLoshkar(@Valid UpdateEmployeeDTO addEmployeeDTO,
                                           @RequestParam(required = false) MultipartFile employeeImage,
                                           @RequestParam(required = false) MultipartFile employeeSignature,
                                           HttpServletRequest request) {
        if (loshkarService.updateEmployee(addEmployeeDTO, employeeImage, employeeSignature, request)) {
            return ResponseEntity.ok("Loshkar Successfully Updated");
        } else {
            return ResponseEntity.badRequest().body("Loshkar Not Updated Successfully");
        }
    }

    @DeleteMapping("/deleteLoshkar")
    public ResponseEntity<?> deleteLoshkar(@RequestParam Long loshkarId,
                                           HttpServletRequest request) {
        if (loshkarService.deleteEmployee(loshkarId, request)) {
            return ResponseEntity.ok("Loshkar Deleted Successfully");
        } else {
            return ResponseEntity.badRequest().body("Loshkar Not Deleted Successfully");
        }
    }
    @GetMapping("/getAllEmployeeDetails")
    public ResponseEntity<?> getAllEmployeeDetails(@RequestParam(defaultValue = "0") int pageNo,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   @RequestParam Long employeeId) {
        return ResponseEntity.ok(loshkarService.getAllEmployeeDetails(pageNo, pageSize, employeeId));
    }

   /* @GetMapping("/profileDetails")
    public ResponseEntity<?> getProfileDetails() {
        String username = EntityUtils.getUserName();
        return ResponseEntity.ok(loshkarService.getProfileDetails(username));
    }*/
}
