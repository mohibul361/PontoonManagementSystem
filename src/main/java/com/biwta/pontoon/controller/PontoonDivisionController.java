package com.biwta.pontoon.controller;

import com.biwta.pontoon.service.PontoonDivisionService;
import com.biwta.pontoon.dto.AddPontoonDivisionDTO;
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
@RequestMapping("/api/v1/pontoon/division")
public class PontoonDivisionController {
    private final PontoonDivisionService pontoonDivisionService;
    @PostMapping("/addPontoonDivision")
    public ResponseEntity<?> addPontoonDivision(@Valid @RequestBody AddPontoonDivisionDTO addPontoonDivisionDTO,
                                                     HttpServletRequest request) {
        if (pontoonDivisionService.addPontoonDivision(addPontoonDivisionDTO, request)!=null)
            return ResponseEntity.ok("PontoonDivision Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonDivision Not Saved Successfully Inserted");
    }
    @GetMapping("/getPontoonDivision")
    public ResponseEntity<?> getPontoonDivision(@RequestParam long pontoonDivisionId) {
        return ResponseEntity.ok(pontoonDivisionService.findPontoonDivisionById(pontoonDivisionId));
    }
    @GetMapping("/getAllPontoonDivision")
    public ResponseEntity<?> getAllPontoonDivision() {
        return ResponseEntity.ok(pontoonDivisionService.findAllPontoonDivision());
    }
    @PutMapping("/updatePontoonDivision")
    public ResponseEntity<?> updatePontoonDivision(@RequestParam long pontoonDivisionId,
                                                   @Valid @RequestBody AddPontoonDivisionDTO addPontoonDivisionDTO,
                                                   HttpServletRequest request) {
        if (pontoonDivisionService.updatePontoonDivision(pontoonDivisionId, addPontoonDivisionDTO, request)!=null)
            return ResponseEntity.ok("PontoonDivision Updated Successfully");
        else
            return ResponseEntity.badRequest().body("PontoonDivision Not Updated Successfully");
    }
    @DeleteMapping("/deletePontoonDivision")
    public ResponseEntity<?> deletePontoonDivision(@RequestParam long pontoonDivisionId,
                                                   HttpServletRequest request) {
        if (pontoonDivisionService.deletePontoon(pontoonDivisionId, request))
            return ResponseEntity.ok("PontoonDivision Deleted Successfully");
        else
            return ResponseEntity.badRequest().body("PontoonDivision Not Deleted Successfully");
    }
}
