package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Port;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.repository.PortRepository;
import com.biwta.pontoon.service.PortService;
import com.biwta.pontoon.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ১৯/১২/২৩
 */
@Service
@Slf4j
@AllArgsConstructor
public class PortServiceImpl implements PortService {
    private final PortRepository portRepository;
    private final TransactionService transactionService;

    @Override
    public Boolean addPort(String portName, HttpServletRequest request) {
        Port port = new Port();
        port.setPortName(portName);
        portRepository.save(port);
        transactionService.addTransaction(new TransactionDTO("Add Port with name is " + portName), request);
        if (port != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Port> routeList(long sectionId) {
        return portRepository.findAll();
    }

    @Override
    public Port getPortById(long id) {
        return portRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Port not found with id:" + id));
    }

    @Override
    public Boolean updatePort(long id, String portName, HttpServletRequest request) {
        return portRepository.findById(id).map(port -> {
            port.setPortName(portName);
            portRepository.save(port);
            transactionService.addTransaction(new TransactionDTO("Update Port with name is " + portName), request);
            return true;
        }).orElseThrow(() -> new RuntimeException("Port not found with id:" + id));
    }
}
