package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddPontoonSectionDTO {
    private String sectionName;
    private Long divisionId;
    private Boolean hasDepartment;
    private Long departmentId;
    private  long district_Id;
    private long division_Id;
}
