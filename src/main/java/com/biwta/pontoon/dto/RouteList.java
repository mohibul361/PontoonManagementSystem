package com.biwta.pontoon.dto;

import lombok.Data;

import java.util.List;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
@Data
public class RouteList {
    private Long id;
    private String routeName;
    private String sectionName;
    private Boolean isActive;
    private List<String> portName;
}
