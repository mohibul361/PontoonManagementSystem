package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Ghat;
import com.biwta.pontoon.dto.AddGhatDTO;
import com.biwta.pontoon.dto.GhatList;
import com.biwta.pontoon.dto.GhatListModel;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface GhatService {
    Boolean addGhat(AddGhatDTO addGhatDTO, HttpServletRequest request);
    Ghat getGhat(Long id);
    GhatListModel getGhatInfo(Long id);
    Page<GhatList> findAllGhat(int pageNo, int pageSize, String searchKeyword);
    Boolean updateGhat(Long pontoonId, AddGhatDTO addGhatDTO, HttpServletRequest request);
    Boolean deleteGhat(Long pontoonId, HttpServletRequest request);

    List<GhatList> findAllGhatBySectionId(Long sectionId);
}
