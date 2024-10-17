package com.biwta.pontoon.dto;

import com.biwta.pontoon.enumuration.BudgetSource;
import com.biwta.pontoon.enumuration.MaintenanceType;
import com.biwta.pontoon.enumuration.Tendering;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Data
public class PontoonMaintenanceDto {
    private long pontoonId;
    private MaintenanceType maintenanceType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date repairDateRange;
    private Double repairCost;
    private long repairedBy;
    private String repairDescription;
    private Integer financialYear;
    private BudgetSource budgetSource;
    private long procuringEntityId;
    private Tendering tenderingMethod;
    private long departmentId;
}
