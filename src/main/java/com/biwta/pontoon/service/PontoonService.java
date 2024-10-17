package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Pontoon;
import com.biwta.pontoon.dto.AddPontoonDTO;
import com.biwta.pontoon.dto.PontoonList;
import com.biwta.pontoon.dto.PontoonListProjection;
import com.biwta.pontoon.dto.UnAssignPontoonList;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonService {
    Boolean addPontoon(AddPontoonDTO addPontoonDTO,  MultipartFile[] pontoonImageArray, HttpServletRequest request);
    Pontoon getPontoon(Long id);
    List<Pontoon>getAllPontoon(List<Long> pontoonIdList);
    List<UnAssignPontoonList> findAllPontoon();
    Page<PontoonListProjection> findAllPontoonWithPagination(int pageNo, int pageSize, String searchKeyword);
    List<PontoonList>assaignPontoonList();
    Boolean updatePontoon(Long pontoonId, AddPontoonDTO addPontoonDTO, MultipartFile pontoonImage, HttpServletRequest request);
    Boolean deletePontoon(Long pontoonId, HttpServletRequest request);

    void updatePontoonAssaignedStatus(long pontoonId);

    List<PontoonListProjection>findAllPontoonList();


}
