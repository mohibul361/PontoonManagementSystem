package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.PontoonSection;
import com.biwta.pontoon.domain.Route;
import com.biwta.pontoon.dto.AddRoute;
import com.biwta.pontoon.dto.RouteList;
import com.biwta.pontoon.dto.RouteListDto;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PortRepository;
import com.biwta.pontoon.repository.RouteRepository;
import com.biwta.pontoon.service.LocationService;
import com.biwta.pontoon.service.PontoonSectionService;
import com.biwta.pontoon.service.RouteService;
import com.biwta.pontoon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final PortRepository portRepository;
    private final TransactionService transactionService;
    private final PontoonSectionService pontoonSectionService;
    private final LocationService locationService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addRoute(AddRoute addRoute, HttpServletRequest request) {
        Route route = new Route();
        if (routeRepository.existsByRouteName(addRoute.getRouteName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route Name Already Exists");
        }
        route.setRouteName(addRoute.getRouteName());
        if (addRoute.getSectionId() == null || addRoute.getSectionId().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Section Id is Required");
        }
        PontoonSection pontoonSection = pontoonSectionService.findPontoonDivisionById(addRoute.getSectionId());
        route.setSection(pontoonSection);
        route.setIsActive(true);
        if(addRoute.getPortId()==0){
            route.setPort(Collections.emptyList());
        }else {
            route.setPort(Collections.singletonList(locationService.getPortById(addRoute.getPortId())));
        }
        routeRepository.save(route);
        transactionService.addTransaction(new TransactionDTO("Add Route with name is " + route.getRouteName()), request);

        if (route != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<RouteListDto> routeList(long sectionId) {
        return routeRepository.findBySection_IdAndIsActiveTrueOrderByIdDesc(sectionId)
                .stream()
                .map(route -> {
                    RouteListDto routeListDto = new RouteListDto();
                    routeListDto.setId(route.getId());
                    routeListDto.setRouteName(route.getRouteName());
                    return routeListDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RouteList> withoutSectionRouteList() {
        return routeRepository.findByIsActiveTrueOrderByIdDesc()
                .stream()
                .map(route -> {
                    RouteList routeListDto = new RouteList();
                    routeListDto.setId(route.getId());
                    routeListDto.setRouteName(route.getRouteName());
                    routeListDto.setSectionName(route.getSection().getSectionName());
                    routeListDto.setIsActive(route.getIsActive());
                    routeListDto.setPortName(route.getPort().stream().map(port -> port.getPortName()).collect(Collectors.toList()));
                    return routeListDto;
                }).collect(Collectors.toList());
    }
}
