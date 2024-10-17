package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nasimkabir
 * ৫/১২/২৩
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhatList {
    private Long id;
    private String ghatName;
    private String sectionName;
    private String divisionName;
    private String districtName;
    private String routeName;
    private Float latitude;
    private Float longitude;
    private long thanaId;
    private String thanaName;
}
