package com.biwta.pontoon.service;

import com.biwta.pontoon.dto.AddLoshkorTransferDto;
import com.biwta.pontoon.dto.LoshkorTransferList;
import com.biwta.pontoon.dto.OldLoshkarTransferInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
public interface LoshkorTransferService {
    Boolean addLochkorTransfer(AddLoshkorTransferDto addLoshkorTransferDto, HttpServletRequest request);
    List<LoshkorTransferList> findAll();
    List<OldLoshkarTransferInfo>oldLoshkarTransferInfo(long employeeId);
}
