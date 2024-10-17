package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonPlacement;
import com.biwta.pontoon.dto.*;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
public interface PontoonPlacementService {
    Boolean addPontoonPlacement(AddPontoonPlacementDTO addPontoonPlacementDTO, HttpServletRequest request);
    PontoonPlacement getPontoonPlacement(Long id);
    PontoonPlacement getPontoonPlacementByLoshkorId(Long loshkorId);
    Page<PontoonPlacementListDto> findAllPontoonPlacement(int pageNo, int pageSize, String searchKeyword);
    Boolean updatePontoonPlacement(Long pontoonPId, AddPontoonPlacementDTO addPontoonPlacementDTO, HttpServletRequest request);
    Boolean deletePontoonPlacement(Long pontoonPId, HttpServletRequest request);

//    Page<CurrentPontoonList> currentPontoonList(int pageNo, int pageSize);
    Page<PontoonList> assaignPontoonList(int pageNo, int pageSize,String searchKeyword);
    List<DashboardData> findAllDashboardData();

    List<PontoonGoogleMapProjection> findAllGoogleMapData(String searchKeyword);

    PontoonDetailsModel findByPontoonIdWithPontoonDetails(Long pontoonId);

}
