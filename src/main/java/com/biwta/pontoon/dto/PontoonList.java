package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author nasimkabir
 * ৫/১২/২৩
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PontoonList {
    private Long id;
    private String pontoonType;
    private String pontoonId;
    private String ghatName;
    private String sectionName;
    private String routeName;
    private Integer buildYear;
    private String manufacturedBy;
    private BigDecimal buildCost;
    private String procuringEntity;
    private String budgetSource;
    private String remarks;
    private Boolean isAssigned;
    private String status;

    public PontoonList(Long id, String pontoonType, String pontoonId, Integer buildYear, String manufacturedBy, BigDecimal buildCost, String procuringEntity, String budgetSource, String remarks, Boolean isAssigned) {
        this.id = id;
        this.pontoonType = pontoonType;
        this.pontoonId = pontoonId;
        this.buildYear = buildYear;
        this.manufacturedBy = manufacturedBy;
        this.buildCost = buildCost;
        this.procuringEntity = procuringEntity;
        this.budgetSource = budgetSource;
        this.remarks = remarks;
        this.isAssigned = isAssigned;
    }
}