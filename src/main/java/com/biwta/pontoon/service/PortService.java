package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Port;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ১৯/১২/২৩
 */
public interface PortService {
    Boolean addPort(String  portName, HttpServletRequest request);
    List<Port> routeList(long sectionId);
    Port getPortById(long id);
    Boolean updatePort(long id, String portName, HttpServletRequest request);
}
