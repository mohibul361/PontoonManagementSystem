package com.biwta.pontoon.dto;

import com.biwta.pontoon.domain.PontoonSize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddPontoonDTO {
    private PontoonSize pontoonSize;
    private String pontoonId;
    private Integer buildYear;
    private String manufacturedBy;
    private BigDecimal buildCost;
    private long procuringEntityId;
    private String budgetSource;
    private String remarks;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingDate;
}
