package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.ProcuringEntry;

import java.util.List;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
public interface ProcuringEntryService {
    Boolean addProcuringEntry(String procuringName,String description);
    List<ProcuringEntry> getAllProcuringEntry();
    ProcuringEntry getProcuringEntryById(Long procuringEntryId);
    Boolean updateProcuringEntry(long procuringEntryId,String procuringName,String description);

    ProcuringEntry deleteProcuringEntry(Long procuringEntryId);

}
