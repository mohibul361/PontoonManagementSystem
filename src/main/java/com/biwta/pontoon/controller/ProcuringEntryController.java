package com.biwta.pontoon.controller;

import com.biwta.pontoon.domain.ProcuringEntry;
import com.biwta.pontoon.service.ProcuringEntryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
@RestController
@RequestMapping("/procuring-entry")
@AllArgsConstructor
public class ProcuringEntryController {
    private final ProcuringEntryService procuringEntryService;

    @PostMapping("/add")
    public Boolean addProcuringEntry(@RequestParam String procuringName, @RequestParam String description) {
        return procuringEntryService.addProcuringEntry(procuringName, description);
    }

    @GetMapping("/all")
    public List<ProcuringEntry> getAllProcuringEntry() {
        return procuringEntryService.getAllProcuringEntry();
    }

    @PutMapping("/update")
    public Boolean updateProcuringEntry(@RequestParam long procuringEntryId, @RequestParam String procuringName, @RequestParam String description) {
        return procuringEntryService.updateProcuringEntry(procuringEntryId, procuringName, description);
    }

    @DeleteMapping("/delete")
    public ProcuringEntry deleteProcuringEntry(@RequestParam Long procuringEntryId) {
        return procuringEntryService.deleteProcuringEntry(procuringEntryId);
    }

    @GetMapping("/get")
    public ProcuringEntry getProcuringEntry(@RequestParam Long procuringEntryId) {
        return procuringEntryService.getProcuringEntryById(procuringEntryId);
    }
}
