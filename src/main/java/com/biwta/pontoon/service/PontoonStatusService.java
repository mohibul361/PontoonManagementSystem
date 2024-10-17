package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonStatus;
import com.biwta.pontoon.dto.PontoonStatusUpdateDto;
import com.biwta.pontoon.dto.PontoonStatusUpdateList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
public interface PontoonStatusService {
    PontoonStatus addPontoonStatus(String statusName);

    Boolean addPontoonStatusUpdate(PontoonStatusUpdateDto pontoonStatusUpdateDto , MultipartFile pontoonImage, HttpServletRequest request);

    List<PontoonStatus> getAllPontoonStatus();

    List<PontoonStatusUpdateList> getAllPontoonStatusUpdate();
}
