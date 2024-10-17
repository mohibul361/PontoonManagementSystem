package com.biwta.pontoon.service;

import com.biwta.pontoon.dto.AddRoute;
import com.biwta.pontoon.dto.RouteList;
import com.biwta.pontoon.dto.RouteListDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
public interface RouteService {
    Boolean addRoute(AddRoute route, HttpServletRequest request);
    List<RouteListDto> routeList(long sectionId);
    List<RouteList> withoutSectionRouteList();

}
