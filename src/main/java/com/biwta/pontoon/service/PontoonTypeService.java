package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.PontoonSize;
import com.biwta.pontoon.domain.PontoonType;
import com.biwta.pontoon.dto.AddPontoonTypeDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonTypeService {
    Boolean addPontoonType(AddPontoonTypeDTO addPontoonTypeDTO, HttpServletRequest request);
    PontoonSize getPontoonType(Long id);
    Page<PontoonSize> findAllPontoonType(int pageNo, int pageSize, String searchKeyword);
    Boolean updatePontoonType(Long pontoonTypeId,AddPontoonTypeDTO addPontoonTypeDTO, HttpServletRequest request);
    Boolean deletePontoonType(Long pontoonTypeId, HttpServletRequest request);
    List<PontoonSize> findAllPontoonTypeWithoutPagination();

    List<PontoonType> findAllPontoonType();
}
