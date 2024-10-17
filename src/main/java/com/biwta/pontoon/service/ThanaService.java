package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Thana;
import com.biwta.pontoon.dto.ThanaList;
import com.biwta.pontoon.repository.ThanaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ৪/২/২৪
 */
@Service
@RequiredArgsConstructor
public class ThanaService {
    private final ThanaRepository thanaRepository;

    public List<ThanaList> getThanaListByDistrictId(Long districtId) {
        return thanaRepository.findByDistrictId(districtId)
                .map(thanas -> thanas.stream()
                        .map(thana -> {
                            ThanaList thanaList = new ThanaList();
                            thanaList.setId(thana.getId());
                            thanaList.setThanaName(thana.getThanaName());
                            return thanaList;
                        }).collect(Collectors.toList()))
                .orElseThrow(()-> new RuntimeException("Thana not found by district id: "+districtId));
    }

    public Thana getThanaById(Long thanaId) {
        return thanaRepository.findById(thanaId)
                .orElseThrow(()-> new RuntimeException("Thana not found by id: "+thanaId));
    }
}
