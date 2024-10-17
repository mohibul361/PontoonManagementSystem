package com.biwta.pontoon.controller;

import com.biwta.pontoon.service.GhatService;
import com.biwta.pontoon.dto.AddGhatDTO;
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
@RequestMapping("/api/v1/ghat")
public class GhatController {
    private final GhatService ghatService;

    @PostMapping("/addGhat")
    public ResponseEntity<String> addGhat(@Valid @RequestBody AddGhatDTO addGhatDTO,
                                          HttpServletRequest request) {
        if (ghatService.addGhat(addGhatDTO, request))
            return ResponseEntity.ok("Ghat Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Ghat Not Saved Successfully Inserted");
    }
    @GetMapping("/getGhat")
    public ResponseEntity<?> getGhat(@RequestParam Long ghatId) {
        return ResponseEntity.ok(ghatService.getGhatInfo(ghatId));
    }
    @GetMapping("/ghatList")
    public ResponseEntity<?> getAllGhat(@RequestParam(defaultValue = "0") int pageNo,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(ghatService.findAllGhat(pageNo, pageSize, searchKeyword));
    }
    @PutMapping("/updateGhat")
    public ResponseEntity<?> updateGhat(@RequestParam Long ghatId,
                                        @Valid @RequestBody AddGhatDTO addGhatDTO,
                                        HttpServletRequest request) {
        if (ghatService.updateGhat(ghatId, addGhatDTO, request)) {
            return ResponseEntity.ok("Ghat Updated Successfully");
        } else {
            return ResponseEntity.badRequest().body("Ghat Not Updated Successfully");
        }
    }
    @DeleteMapping("/deleteGhat")
    public ResponseEntity<?> deleteGhat(@RequestParam Long ghatId,
                                        HttpServletRequest request) {
        if (ghatService.deleteGhat(ghatId, request)) {
            return ResponseEntity.ok("Ghat Deleted Successfully");
        } else {
            return ResponseEntity.badRequest().body("Ghat Not Deleted Successfully");
        }
    }

    @GetMapping("/ghatListBySectionId")
    public ResponseEntity<?> getAllGhatBySectionId(@RequestParam Long sectionId) {
        return ResponseEntity.ok(ghatService.findAllGhatBySectionId(sectionId));
    }
}
