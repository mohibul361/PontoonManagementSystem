package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ১৫/১/২৪
 */
@Data
public class PontoonSectionModel {
    private Long id;
    private String sectionName;
    private Boolean isActive;
    private long pontoonDivisionId;
    private String pontoonDivisionName;
    private Boolean hasDepartment;
    private long departmentId;
    private String departmentName;
    private Boolean isAssigned;
    private long divisionId;
    private String divisionName;
    private long districtId;
    private String districtName;
    private long thanaId;
    private String thanaName;
}
