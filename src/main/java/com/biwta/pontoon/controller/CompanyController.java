package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddCompany;
import com.biwta.pontoon.service.CompanyService;
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
@RequestMapping("/api/v1/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companyList")
    public ResponseEntity<?> companyList() {
        return ResponseEntity.ok(companyService.companyList());
    }

    @PostMapping("/addCompany")
    public ResponseEntity<String> addCompany(@Valid @RequestBody AddCompany addCompany,
                                             HttpServletRequest request) {
        if (companyService.addCompany(addCompany, request) != null)
            return ResponseEntity.ok("Company Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Company Successfully Inserted");
    }
    @PutMapping("/updateCompany")
    public ResponseEntity<String>updateCompany(@Valid @RequestBody AddCompany updateCompany,
                                               @RequestParam long companyId,
                                               HttpServletRequest request){
        if (companyService.updateCompany(companyId,updateCompany,request)!=null)
            return ResponseEntity.ok("Company Successfully Updated");
        else
            return ResponseEntity.badRequest().body("Company Successfully Updated");
    }
    @DeleteMapping("/deleteCompany")
    public ResponseEntity<String>deleteCompany(@RequestParam long companyId,
                                               HttpServletRequest request){
        companyService.deleteCompany(companyId,request);
        return ResponseEntity.ok("Company Successfully Deleted");
    }
}
