package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.ThanaList;
import com.biwta.pontoon.service.ThanaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nasimkabir
 * ৪/২/২৪
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/thana")
public class ThanaController {
    private final ThanaService thanaService;

    @GetMapping("/thanaListByDistrictId")
    public List<ThanaList> getThanaListByDistrictId(Long districtId) {
        return thanaService.getThanaListByDistrictId(districtId);
    }
}
