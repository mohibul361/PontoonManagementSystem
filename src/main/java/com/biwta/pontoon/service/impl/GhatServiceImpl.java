package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Ghat;
import com.biwta.pontoon.domain.Route;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.AddGhatDTO;
import com.biwta.pontoon.dto.GhatList;
import com.biwta.pontoon.dto.GhatListModel;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.GhatRepository;
import com.biwta.pontoon.repository.RouteRepository;
import com.biwta.pontoon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GhatServiceImpl implements GhatService {
    private final GhatRepository ghatRepository;
    private final TransactionService transactionService;
    private final PontoonSectionService pontoonSectionService;
    private final RouteRepository routeRepository;
    private final LocationService locationService;
    private final ThanaService thanaService;
    private final PortService portService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addGhat(AddGhatDTO addGhatDTO, HttpServletRequest request) {
        Route route = routeRepository.findById(addGhatDTO.getRouteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route Not Found With Id " + addGhatDTO.getRouteId()));
        Ghat ghat = new Ghat();
        if (ghatRepository.existsByGhatName(addGhatDTO.getGhatName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ghat Name Already Exists");
        }
        ghat.setGhatName(addGhatDTO.getGhatName());
        ghat.setSection(pontoonSectionService.findPontoonDivisionById(addGhatDTO.getSectionId()));
        ghat.setRoute(route);
        ghat.setLatitude(addGhatDTO.getLatitude());
        ghat.setLongitude(addGhatDTO.getLongitude());
        ghat.setDivision(locationService.getDivisionById(addGhatDTO.getDivisionId()));
        ghat.setDistrict(locationService.getDistrictById(addGhatDTO.getDistrictId()));
        ghat.setThana(thanaService.getThanaById(addGhatDTO.getThanaId()));
        ghat.setPort(portService.getPortById(addGhatDTO.getPortId()));
        ghat.setIsActive(true);
        ghatRepository.save(ghat);
        Transaction t = transactionService.addTransaction(new TransactionDTO("Ghat Added"), request);
        if (ghat != null && t != null) {
            log.info("Ghat Added Successfully");
            return true;
        } else {
            log.info("Ghat Not Added Successfully");
            return false;
        }
    }

    @Override
    public Ghat getGhat(Long id) {
        return this.ghatRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ghat Not Found With Id " + id));
    }

    @Override
    public GhatListModel getGhatInfo(Long id) {
        return this.ghatRepository.findByIdAndIsActiveTrue(id)
                .stream().map(ghat -> {
                    GhatListModel ghatListModel = new GhatListModel();
                    ghatListModel.setId(ghat.getId());
                    ghatListModel.setGhatName(ghat.getGhatName());
                    ghatListModel.setSectionId(ghat.getSection().getId());
                    ghatListModel.setSectionName(ghat.getSection().getSectionName());
                    ghatListModel.setRouteId(ghat.getRoute().getId());
                    ghatListModel.setRouteName(ghat.getRoute().getRouteName());
                    ghatListModel.setDivisionId(ghat.getDivision().getId());
                    ghatListModel.setDivisionName(ghat.getDivision().getDivisionName());
                    ghatListModel.setDistrictId(ghat.getDistrict().getId());
                    ghatListModel.setDistrictName(ghat.getDistrict().getDistrictName());
                    ghatListModel.setThanaId(ghat.getThana().getId());
                    ghatListModel.setThanaName(ghat.getThana().getThanaName());
                    ghatListModel.setLatitude(ghat.getLatitude().toString());
                    ghatListModel.setLongitude(ghat.getLongitude().toString());
                    ghatListModel.setIsActive(ghat.getIsActive());
                    return ghatListModel;
                }).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ghat Not Found With Id " + id));
    }

    @Override
    public Page<GhatList> findAllGhat(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            Page<Ghat> ghats = ghatRepository.findAllByIsActiveTrueOrderByIdDesc(pageable);
            if (ghats.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
            }
            return ghats.map(ghat -> new GhatList(ghat.getId(), ghat.getGhatName(), ghat.getSection().getSectionName(),
                    ghat.getDivision().getDivisionName(), ghat.getDistrict().getDistrictName(), ghat.getRoute().getRouteName(),
                    ghat.getLatitude(), ghat.getLongitude(),ghat.getThana().getId(),ghat.getThana().getThanaName()));
        } else {
            Page<Ghat> ghats = ghatRepository.findAllByKeyword(searchKeyword, pageable);
            if (ghats.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found for the given keyword");
            }
            return ghats.map(ghat -> new GhatList(ghat.getId(), ghat.getGhatName(), ghat.getSection().getSectionName(),
                    ghat.getDivision().getDivisionName(), ghat.getDistrict().getDistrictName(), ghat.getRoute().getRouteName(),
                    ghat.getLatitude(), ghat.getLongitude(),ghat.getThana().getId(),ghat.getThana().getThanaName()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateGhat(Long pontoonId, AddGhatDTO addGhatDTO, HttpServletRequest request) {
        return this.ghatRepository.findById(pontoonId)
                .map(ghat -> {
                   /* if (ghatRepository.existsByGhatName(addGhatDTO.getGhatName())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ghat Name Already Exists");
                    }*/
                    ghat.setGhatName(addGhatDTO.getGhatName());
                    ghat.setSection(pontoonSectionService.findPontoonDivisionById(addGhatDTO.getSectionId()));
                    ghat.setLatitude(addGhatDTO.getLatitude());
                    ghat.setLongitude(addGhatDTO.getLongitude());
                    ghatRepository.save(ghat);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Ghat Updated"), request);
                    if (t != null) {
                        log.info("Ghat Updated Successfully");
                        return true;
                    } else {
                        log.info("Ghat Not Updated Successfully");
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ghat Not Found"));
    }

    @Override
    public Boolean deleteGhat(Long pontoonId, HttpServletRequest request) {
        return this.ghatRepository.findById(pontoonId)
                .map(ghat -> {
                    ghat.setIsActive(false);
                    ghatRepository.save(ghat);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Ghat Deleted"), request);
                    if (t != null) {
                        log.info("Ghat Deleted Successfully");
                        return true;
                    } else {
                        log.info("Ghat Not Deleted Successfully");
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ghat Not Found"));
    }

    @Override
    public List<GhatList> findAllGhatBySectionId(Long sectionId) {
        List<Ghat> ghatList= ghatRepository.findBySectionIdAndIsActiveTrue(sectionId);
        if(ghatList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        return ghatList.stream()
                .map(ghat -> new GhatList(ghat.getId(), ghat.getGhatName(), ghat.getSection().getSectionName(),
                        ghat.getRoute().getRouteName(), ghat.getDivision().getDivisionName(), ghat.getDistrict().getDistrictName(),
                        ghat.getLatitude(), ghat.getLongitude(),ghat.getThana().getId(),ghat.getThana().getThanaName())).collect(Collectors.toList());
    }
}
