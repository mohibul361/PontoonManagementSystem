package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Employee;
import com.biwta.pontoon.domain.LoshkorTransfer;
import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.AddLoshkorTransferDto;
import com.biwta.pontoon.dto.LoshkorTransferList;
import com.biwta.pontoon.dto.OldLoshkarTransferInfo;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.LoshkorTransferRepository;
import com.biwta.pontoon.service.LoshkarService;
import com.biwta.pontoon.service.LoshkorTransferService;
import com.biwta.pontoon.service.PontoonService;
import com.biwta.pontoon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoshkorTransferServiceImpl implements LoshkorTransferService {
    private final LoshkorTransferRepository loshkorTransferRepository;
    private final TransactionService transactionService;
    private final LoshkarService loshkarService;
    private final PontoonService pontoonService;

    @Override
    public Boolean addLochkorTransfer(AddLoshkorTransferDto addLoshkorTransferDto, HttpServletRequest request) {
        List<LoshkorTransfer> existingTransfers = loshkorTransferRepository.findAllByEmployee_Id(addLoshkorTransferDto.getLoshkorId());

        if (!existingTransfers.isEmpty()) {
            // Handle multiple existing transfers if needed
            for (LoshkorTransfer existingTransferRecord : existingTransfers) {
                existingTransferRecord.setTillDate(Date.from(addLoshkorTransferDto.getTransferDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                loshkorTransferRepository.save(existingTransferRecord);
            }
        }

        Employee employee = loshkarService.getEmployee(addLoshkorTransferDto.getLoshkorId());
        LoshkorTransfer loshkorTransfer = new LoshkorTransfer();
        loshkorTransfer.setEmployee(employee);
        loshkorTransfer.setAddDate(Date.from(addLoshkorTransferDto.getTransferDate().toInstant()));
        loshkorTransfer.setEffectiveDate(addLoshkorTransferDto.getTransferDate());
        loshkorTransfer.setTillDate(Date.from(Date.from(Instant.now()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().plusYears(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        loshkorTransfer.setPontoon(pontoonService.getAllPontoon(addLoshkorTransferDto.getPontoonId()));
        loshkorTransferRepository.save(loshkorTransfer);
        Transaction t=transactionService.addTransaction(new TransactionDTO("Loshkor Transfer successfully Added with loshkorId is "+employee.getPmsId()),request);
        return true;
    }

    @Override
    public List<LoshkorTransferList> findAll() {
        return loshkorTransferRepository.findAll()
                .stream()
                .map(loshkorTransfer -> {
                    LoshkorTransferList loshkorTransferList = new LoshkorTransferList();
                    loshkorTransferList.setId(loshkorTransfer.getId());
                    loshkorTransferList.setEmployeeName(loshkorTransfer.getEmployee().getEmployeeName());
                    loshkorTransfer.getPontoon().forEach(pontoon -> {
                        loshkorTransferList.setPontoonId(pontoon.getPontoonId());
                    });
                    return loshkorTransferList;
                }).collect(Collectors.toList());
    }

    @Override
    public List<OldLoshkarTransferInfo> oldLoshkarTransferInfo(long employeeId) {
        List<OldLoshkarTransferInfo> oldLoshkorTransferLists= loshkorTransferRepository.oldLoshkarTransferInfo(employeeId);
        if(oldLoshkorTransferLists.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Data Found");
        }
        return oldLoshkorTransferLists;
    }


}
