package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.*;
import com.biwta.pontoon.dto.*;
import com.biwta.pontoon.repository.*;
import com.biwta.pontoon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PontoonPlacementServiceImpl implements PontoonPlacementService {
    private final PontoonPlacementRepository pontoonPlacementRepository;
    private final PontoonService pontoonService;
    private final GhatRepository ghatRepository;
    private final RouteRepository routeRepository;
    private final TransactionService transactionService;
    private final LoshkarService loshkarService;
    private final PontoonTransferRepository pontoonTransferRepository;
    private final LoshkorTransferRepository loshkorTransferRepository;
    private final LocationService locationService;
    private final PontoonStatusRepository pontoonStatusRepository;
    private final PontoonStatusUpdateRepository pontoonStatusUpdateRepository;

    @Override
    public Boolean addPontoonPlacement(AddPontoonPlacementDTO addPontoonPlacementDTO, HttpServletRequest request) {
        if (pontoonPlacementRepository.existsByPontoonId(addPontoonPlacementDTO.getPontoonId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pontoon Already Placement");
        }
        Pontoon pontoon = pontoonService.getPontoon(addPontoonPlacementDTO.getPontoonId());
        Ghat ghat = ghatRepository.findById(addPontoonPlacementDTO.getGhatId()).get();
        PontoonPlacement pontoonPlacement = new PontoonPlacement();
        pontoonPlacement.setPontoon(pontoon);
        pontoonPlacement.setGhat(ghat);
        pontoonPlacement.setRoute(routeRepository.getById(addPontoonPlacementDTO.getRouteId()));
        pontoonPlacement.setRemarks(addPontoonPlacementDTO.getRemarks());
        pontoonPlacement.setLatitude(addPontoonPlacementDTO.getLatitude());
        pontoonPlacement.setLongitude(addPontoonPlacementDTO.getLongitude());
        pontoonPlacement.setIsActive(true);
        pontoonPlacement.setDivision(locationService.getDivisionById(addPontoonPlacementDTO.getDivisionId()));
        pontoonPlacement.setDistrict(locationService.getDistrictById(addPontoonPlacementDTO.getDistrictId()));


        List<Employee> employeeList = new ArrayList<>();
        addPontoonPlacementDTO.getEmployees().forEach(employeeId -> {
            employeeList.add(loshkarService.getEmployee(employeeId));
        });
        pontoonPlacement.setEmployee(employeeList);
        pontoonPlacement = pontoonPlacementRepository.save(pontoonPlacement);
        Transaction t = transactionService.addTransaction(new TransactionDTO("Pontoon Placement successfully Added with pontonId is " + pontoon.getPontoonId()), request);
        // update pontoon assaigned status
        pontoonService.updatePontoonAssaignedStatus(addPontoonPlacementDTO.getPontoonId());

        // also insert into pontoon transfer table
        PontoonTransfer pontoonTransfer = new PontoonTransfer();
        pontoonTransfer.setPontoon(pontoon);
        pontoonTransfer.setSection(ghat.getSection());
        pontoonTransfer.setGhat(ghat);
        pontoonTransfer.setLatitude(addPontoonPlacementDTO.getLatitude());
        pontoonTransfer.setLongitude(addPontoonPlacementDTO.getLongitude());
        pontoonTransfer.setTransferDate(Date.from(Instant.now()));
        pontoonTransfer.setTillDate(Date.from(Date.from(Instant.now()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().plusYears(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        pontoonTransfer.setEmployee(employeeList);
        pontoonTransferRepository.save(pontoonTransfer);
        transactionService.addTransaction(new TransactionDTO("Pontoon Transfer successfully Added with pontonId is " + pontoon.getPontoonId()), request);

        // and insert into loshkor transfer table
        // store loshkar transfer
        LoshkorTransfer loshkorTransfer = new LoshkorTransfer();
        addPontoonPlacementDTO.getEmployees().forEach(employeeId -> {
            loshkorTransfer.setEmployee(loshkarService.getEmployee(employeeId));
        });
        loshkorTransfer.setAddDate(Date.from(Instant.now()));
        loshkorTransfer.setEffectiveDate(Date.from(Instant.now()));
        loshkorTransfer.setTillDate(Date.from(Date.from(Instant.now()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().plusYears(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        loshkorTransfer.setPontoon(Collections.singletonList(pontoon));
        loshkorTransferRepository.save(loshkorTransfer);

        // Save pontoon status
        PontoonStatus status = pontoonStatusRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontoon Status not found with id: 1"));
        PontoonStatusUpdate pontoonStatusUpdate = new PontoonStatusUpdate();
        pontoonStatusUpdate.setPontoon(pontoon);
        pontoonStatusUpdate.setStatus(status);
        pontoonStatusUpdate.setDocumentUrl(null);
        pontoonStatusUpdate.setDescription("Pontoon added");
        pontoonStatusUpdateRepository.save(pontoonStatusUpdate);
        transactionService.addTransaction(new TransactionDTO("Loshkor Transfer successfully Added with loshkorId is " + loshkorTransfer.getEmployee().getPmsId()), request);
        if (pontoonPlacement != null && t != null) {
            log.info("Pontoon Placement Added Successfully");
            return true;
        } else {
            log.info("Pontoon Placement Not Added Successfully");
            return false;
        }
    }

    @Override
    public PontoonPlacement getPontoonPlacement(Long id) {
        return pontoonPlacementRepository.findByIsActiveTrueAndId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Placement not found with id: " + id));
    }

    @Override
    public PontoonPlacement getPontoonPlacementByLoshkorId(Long loshkorId) {
        return pontoonPlacementRepository.findByEmployee_Id(loshkorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Placement not found with id: " + loshkorId));
    }

    @Override
    public Page<PontoonPlacementListDto> findAllPontoonPlacement(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            Page<PontoonPlacement> pontoon = pontoonPlacementRepository.findAllByIsActiveTrue(pageable);
            if (pontoon.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
            }
            return pontoon.map(this::convertToDto);
        } else {
            Page<PontoonPlacement> pontoon = pontoonPlacementRepository.findAllByKeyword(searchKeyword, pageable);
            if (pontoon.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found for the given keyword");
            }
            // Convert Page<PontoonPlacement> to Page<PontoonPlacementListDto>
            return pontoon.map(this::convertToDto);
        }
    }

    @Override
    public Boolean updatePontoonPlacement(Long pontoonPId, AddPontoonPlacementDTO addPontoonPlacementDTO, HttpServletRequest request) {
        return pontoonPlacementRepository.findByIsActiveTrueAndPontoonId(pontoonPId)
                .map(pontoonPlacement -> {
                    if (addPontoonPlacementDTO.getPontoonId() != 0) {
                        pontoonPlacement.setPontoon(pontoonService.getPontoon(addPontoonPlacementDTO.getPontoonId()));
                    }
                    if (addPontoonPlacementDTO.getGhatId() != 0) {
                        pontoonPlacement.setGhat(ghatRepository.findById(addPontoonPlacementDTO.getGhatId()).orElse(null));
                    }
                    if (addPontoonPlacementDTO.getRouteId() != 0) {
                        pontoonPlacement.setRoute(routeRepository.findById(addPontoonPlacementDTO.getRouteId()).orElse(null));
                    }
                    if (addPontoonPlacementDTO.getRemarks() != null) {
                        pontoonPlacement.setRemarks(addPontoonPlacementDTO.getRemarks());
                    }
                    if (addPontoonPlacementDTO.getLatitude() != null) {
                        pontoonPlacement.setLatitude(addPontoonPlacementDTO.getLatitude());
                    }
                    if (addPontoonPlacementDTO.getLongitude() != null) {
                        pontoonPlacement.setLongitude(addPontoonPlacementDTO.getLongitude());
                    }

                    pontoonPlacement = pontoonPlacementRepository.save(pontoonPlacement);

                    Transaction t = transactionService.addTransaction(
                            new TransactionDTO("Pontoon Placement successfully Updated with pontonId is " + pontoonPlacement.getPontoon().getPontoonId()), request);

                    if (pontoonPlacement != null && t != null) {
                        log.info("Pontoon Placement Updated Successfully");
                        return true;
                    } else {
                        log.info("Pontoon Placement Not Updated Successfully");
                        return false;
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon Placement not found with id: " + pontoonPId));
    }


    @Override
    public Boolean deletePontoonPlacement(Long pontoonPId, HttpServletRequest request) {
        return null;
    }

    /* @Override
     public Page<CurrentPontoonList> currentPontoonList(int pageNo, int pageSize) {
         Pageable pageable = PageRequest.of(pageNo, pageSize);
         return pontoonPlacementRepository.findByIsActiveTrueAndPontoonIsAssignedTrue(pageable)
                 .map(currentdata -> {
                     CurrentPontoonList currentPontoonList = new CurrentPontoonList();
                     currentPontoonList.setPontoonId(currentdata.getPontoon().getPontoonId());
                     currentPontoonList.setPontoonName(currentdata.getPontoon().getPontoonId());
                     currentPontoonList.setGhatName(currentdata.getGhat().getGhatName());
                     currentPontoonList.setLatitude(currentdata.getLatitude());
                     currentPontoonList.setLatitude(currentdata.getLongitude());
                     return currentPontoonList;
                 });
     }
 */
    @Override
    public Page<PontoonList> assaignPontoonList(int pageNo, int pageSize,String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PontoonPlacementProjection> data =
                (searchKeyword == null || searchKeyword.isEmpty())
                        ? pontoonPlacementRepository.findByIsActiveTrueAndPontoonIsAssignedTrueOrderByCreatedDate(pageable)
                        : pontoonPlacementRepository.findByIsActiveTrueAndPontoonIsAssignedTrueSearchKeyword(pageable,searchKeyword);
        if(data.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        return data
                .map(currentdata -> {
                    PontoonList pontoonList = new PontoonList();
                    pontoonList.setId(currentdata.getId());
                    pontoonList.setPontoonId(currentdata.getPontoonId());
                    pontoonList.setPontoonType(currentdata.getPontoonType());
                    pontoonList.setGhatName(currentdata.getGhatName());
                    pontoonList.setSectionName(currentdata.getSectionName());
                    pontoonList.setRouteName(currentdata.getRouteName());
                    pontoonList.setBuildYear(currentdata.getBuildYear());
                    pontoonList.setManufacturedBy(currentdata.getManufacturedBy());
                    pontoonList.setBuildCost(currentdata.getBuildCost());
                    pontoonList.setProcuringEntity(currentdata.getProcuringEntity());
                    pontoonList.setBudgetSource(String.valueOf(currentdata.getBudgetSource()));
                    pontoonList.setRemarks(currentdata.getRemarks());
                    pontoonList.setIsAssigned(currentdata.getIsAssigned());
                    pontoonList.setStatus(currentdata.getStatus());
                    return pontoonList;
                });
    }

    @Override
    public List<DashboardData> findAllDashboardData() {
        List<DashboardData> dashboardData = pontoonPlacementRepository.findAllDashboardData();
        if (dashboardData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        return dashboardData;
    }

    @Override
    public List<PontoonGoogleMapProjection> findAllGoogleMapData(String searchKeyword) {
        List<PontoonGoogleMapProjection> googleMapData =
                (searchKeyword == null || searchKeyword.isEmpty())
                        ? pontoonPlacementRepository.findAllPontoonGoogleMapProjectionWithoutSearch()
                        : pontoonPlacementRepository.findAllPontoonGoogleMapProjectionWithBySearch(searchKeyword);

        if (googleMapData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }

        return googleMapData;
    }

    @Override
    public PontoonDetailsModel findByPontoonIdWithPontoonDetails(Long pontoonId) {
        PontoonDetailsProjection pontoonDetails = pontoonPlacementRepository.findByPontoonIdWithPontoonDetails(pontoonId);
        if (pontoonDetails == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        PontoonDetailsModel pontoonDetailsModel = new PontoonDetailsModel();
        pontoonDetailsModel.setPontoonId(pontoonDetails.getPontoonId());
        pontoonDetailsModel.setPontoonType(pontoonDetails.getPontoonType());
        pontoonDetailsModel.setGhatName(pontoonDetails.getGhatName());
        pontoonDetailsModel.setSectionName(pontoonDetails.getSectionName());
        pontoonDetailsModel.setRouteName(pontoonDetails.getRouteName());
        pontoonDetailsModel.setBuildYear(pontoonDetails.getBuildYear());
        pontoonDetailsModel.setManufacturedBy(pontoonDetails.getManufacturedBy());
        pontoonDetailsModel.setBuildCost(pontoonDetails.getBuildCost());
        pontoonDetailsModel.setProcuringEntity(pontoonDetails.getProcuringEntity());
        pontoonDetailsModel.setBudgetSource(pontoonDetails.getBudgetSource());
        pontoonDetailsModel.setLatitude(pontoonDetails.getLatitude());
        pontoonDetailsModel.setLongitude(pontoonDetails.getLongitude());
        pontoonDetailsModel.setPontoonStatus(pontoonDetails.getPontoonStatus());
        pontoonDetailsModel.setPontoonImage(pontoonDetails.getPontoonImage() == null ? "" : pontoonDetails.getPontoonImage());
        List<LoshkarDetailsProjection> loshkarDetailsProjections = pontoonPlacementRepository.findAllByPontoonIdWithLoshkarDetails(pontoonId);
        log.info("LoshkarDetailsProjection size: {}", loshkarDetailsProjections.size());
        if (loshkarDetailsProjections.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        loshkarDetailsProjections.forEach(loshkarDetailsProjection -> {
            LoshkarDetailsModel loshkarDetailsModel = new LoshkarDetailsModel();
            loshkarDetailsModel.setEmployeeId(loshkarDetailsProjection.getEmployeeId());
            loshkarDetailsModel.setEmployeeName(loshkarDetailsProjection.getEmployeeName());
            loshkarDetailsModel.setEmployeeImage(loshkarDetailsProjection.getEmployeeImage() == null ? "" : loshkarDetailsProjection.getEmployeeImage());
            pontoonDetailsModel.getLoshkarDetails().add(loshkarDetailsModel);
        });
        return pontoonDetailsModel;
    }

    private PontoonPlacementListDto convertToDto(PontoonPlacement pontoonPlacement) {
        PontoonPlacementListDto pontoonPlacementListDto = new PontoonPlacementListDto();
        pontoonPlacementListDto.setId(pontoonPlacement.getId());
        pontoonPlacementListDto.setPontoonName(pontoonPlacement.getPontoon().getPontoonId());
        pontoonPlacementListDto.setPontoonId(pontoonPlacement.getPontoon().getId());
        pontoonPlacementListDto.setGhatName(pontoonPlacement.getGhat().getGhatName());
        pontoonPlacementListDto.setGhatId(pontoonPlacement.getGhat().getId());
        pontoonPlacementListDto.setRouteId(pontoonPlacement.getRoute().getId());
        pontoonPlacementListDto.setRouteName(pontoonPlacement.getRoute().getRouteName());
        pontoonPlacementListDto.setRemarks(pontoonPlacement.getRemarks());
        pontoonPlacementListDto.setLongitude(pontoonPlacement.getLongitude());
        pontoonPlacementListDto.setLatitude(pontoonPlacement.getLatitude());
        pontoonPlacement.getEmployee().forEach(
                employee -> {
                    pontoonPlacementListDto.setEmployeeId(employee.getId());
                    pontoonPlacementListDto.setEmployeeName(employee.getEmployeeName());
                }
        );

        return pontoonPlacementListDto;
    }
}
