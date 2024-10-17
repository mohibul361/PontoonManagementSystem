package com.biwta.pontoon.controller;

import com.biwta.pontoon.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationService divisionService;

    @GetMapping("/divisionList")
    public ResponseEntity<?> divisionList() {
        return ResponseEntity.ok(divisionService.getAllDivision());
    }

    @GetMapping("/districtList")
    public ResponseEntity<?> districtList(@RequestParam Long divisionId) {
        return ResponseEntity.ok(divisionService.getAllByDivision(divisionId));
    }

    @GetMapping("/portList")
    public ResponseEntity<?> portList() {
        return ResponseEntity.ok(divisionService.portList());
    }
}
