package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.District;
import com.biwta.pontoon.domain.Division;
import com.biwta.pontoon.domain.Port;
import com.biwta.pontoon.dto.DistrictListDto;
import com.biwta.pontoon.dto.DivisionListDto;
import com.biwta.pontoon.dto.PortList;
import com.biwta.pontoon.repository.DistrictRepository;
import com.biwta.pontoon.repository.DivisionRepository;
import com.biwta.pontoon.repository.PortRepository;
import com.biwta.pontoon.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final RouteRepository routeRepository;
    private final PortRepository portRepository;

    public List<DivisionListDto> getAllDivision(){

        return divisionRepository.findAll()
                .stream()
                .map(division -> DivisionListDto.builder()
                        .id(division.getId())
                        .divisionName(division.getDivisionName())
                        .build())
                .collect(Collectors.toList());
    }
    public Division getDivisionById(Long divisionId){

        return divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division Not Found With Id "+divisionId));
    }

    public List<DistrictListDto> getAllByDivision(Long divisionId) {
        return districtRepository.findAllByDivision_IdAndIsActiveTrueOrderById(divisionId)
                .stream()
                .map(district -> DistrictListDto.builder()
                        .id(district.getId())
                        .districtName(district.getDistrictName())
                        .build())
                .collect(Collectors.toList());
    }

    public District getDistrictById(Long districtId) {
        return districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District Not Found With Id "+districtId));
    }

    public List<PortList> portList() {
       return portRepository.findAll()
                .stream()
                .map(route -> PortList.builder()
                        .id(route.getId())
                        .portName(route.getPortName())
                        .build())
                .collect(Collectors.toList());
    }

    public Port getPortById(Long portId) {
        return portRepository.findById(portId)
                .orElseThrow(() -> new RuntimeException("Port Not Found With Id "+portId));
    }
}
