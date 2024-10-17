package com.biwta.pontoon.service;

import com.biwta.pontoon.dto.AddPontoonTransferDto;
import com.biwta.pontoon.dto.OldPontoonTransferInfo;
import com.biwta.pontoon.dto.PontoonTransferList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ৩০/১১/২৩
 */
public interface PontoonTransferService {
    Boolean addPontoonTransfer(AddPontoonTransferDto addPontoonTransferDto, MultipartFile documentsFile, HttpServletRequest request);
    List<PontoonTransferList> pontoonTransferList();

    List<OldPontoonTransferInfo> oldPontoonTransferInfo(long pontoonId);
}
