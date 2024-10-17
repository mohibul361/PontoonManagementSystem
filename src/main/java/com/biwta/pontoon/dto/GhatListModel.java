package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ২১/১২/২৩
 */
@Data
public class GhatListModel {
    private Long id;
    private String ghatName;
    private long sectionId;
    private String sectionName;
    private long divisionId;
    private String divisionName;
    private long districtId;
    private String districtName;
    private long thanaId;
    private String thanaName;
    private long routeId;
    private String routeName;
    private String latitude;
    private String longitude;
    private Boolean isActive;
}
