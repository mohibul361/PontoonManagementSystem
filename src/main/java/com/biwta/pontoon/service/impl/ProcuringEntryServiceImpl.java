package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.ProcuringEntry;
import com.biwta.pontoon.repository.ProcuringEntryRepository;
import com.biwta.pontoon.service.ProcuringEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
@Service
@RequiredArgsConstructor
public class ProcuringEntryServiceImpl implements ProcuringEntryService {
    private final ProcuringEntryRepository procuringEntryRepository;

    public Boolean addProcuringEntry(String procuringName,String description) {
        ProcuringEntry procuringEntry = new ProcuringEntry();
        procuringEntry.setIsActive(true);
        procuringEntry.setDescription(description);
        if (procuringEntryRepository.existsByProcuringName(procuringName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Procuring Name already exists");
        }
        procuringEntry.setProcuringName(procuringName);
        ProcuringEntry p = procuringEntryRepository.save(procuringEntry);
        if (p != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ProcuringEntry> getAllProcuringEntry() {
        return procuringEntryRepository.findAllByIsActiveTrue();
    }

    @Override
    public ProcuringEntry getProcuringEntryById(Long procuringEntryId) {
        return procuringEntryRepository.findById(procuringEntryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procuring Entry not found with id " + procuringEntryId));
    }

    @Override
    public Boolean updateProcuringEntry(long procuringEntryId, String procuringName, String description) {
        return procuringEntryRepository.findById(procuringEntryId).map(procuringEntry->{
            procuringEntry.setProcuringName(procuringName);
            procuringEntry.setDescription(description);
             procuringEntryRepository.save(procuringEntry);
            return true;
        }).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Procuring entry not found with id "+procuringEntryId));
    }

    @Override
    public ProcuringEntry deleteProcuringEntry(Long procuringEntryId) {
        return procuringEntryRepository.findById(procuringEntryId).map(procuringEntry -> {
            procuringEntry.setIsActive(false);
            return procuringEntryRepository.save(procuringEntry);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procuring Entry not found with id " + procuringEntryId));
    }

}
