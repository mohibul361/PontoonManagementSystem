package com.biwta.pontoon.dto;

import com.biwta.pontoon.domain.PontoonSize;
import com.biwta.pontoon.domain.ProcuringEntry;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ৬/২/২৪
 */
@Data
public class UnAssignPontoonList {
    private Long id;
    private PontoonSize pontoonType;
    private String pontoonId;
    private Integer buildYear;
    private String manufacturedBy;
    private BigDecimal buildCost;
    private ProcuringEntry procuringEntity;
    private String budgetSource;
    private String remarks;
    private Boolean isActive;
    private List<PontoonImageModel> pontoonImage=new ArrayList<>();
    private Boolean isAssigned;
    private Date receivingDate;
}
