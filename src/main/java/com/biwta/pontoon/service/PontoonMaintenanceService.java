package com.biwta.pontoon.service;

import com.biwta.pontoon.dto.MaitenanceListProjection;
import com.biwta.pontoon.dto.PontoonMaintenanceDto;
import com.biwta.pontoon.dto.PontoonMaintenanceList;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
public interface PontoonMaintenanceService {
    Boolean addPontoonMaintenance(PontoonMaintenanceDto pontoonMaintenanceDto, MultipartFile pontoonMainImage, MultipartFile ltmDocumentUrl
            , MultipartFile approvalDocumentUrl, HttpServletRequest request);

    Page<PontoonMaintenanceList> getAllPontoonMaintenance(int page, int size,String searchKey);

    Boolean updatePontoonMaintenance(long pontoonMaintenanceId, PontoonMaintenanceDto pontoonMaintenanceDto, MultipartFile pontoonMainImage, MultipartFile ltmDocumentUrl
            , MultipartFile approvalDocumentUrl, HttpServletRequest request);

    PontoonMaintenanceList getPontoonMaintenanceById(Long pontoonMaintenanceId);

    List<MaitenanceListProjection> findAllMaintenanceList();
}
