package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ৫/১২/২৩
 */
@Data
public class PontoonMaintenanceList {
    private Long id;
    private long pontoonId;
    private String pontoonName;
    private String maintenanceType;
    private String repairDateRange;
    private Double repairCost;
    private long companyId;
    private String repairedBy;
    private String repairDescription;
    private Integer financialYear;
    private String budgetSource;
    private String procuringEntity;
    private String tenderingMethod;
    private String departmentName;
}
