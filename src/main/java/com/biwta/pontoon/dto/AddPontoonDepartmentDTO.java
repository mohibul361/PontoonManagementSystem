package com.biwta.pontoon.dto;

import com.biwta.pontoon.domain.Division;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddPontoonDepartmentDTO {
    @NotNull(message = "Department name can not be empty")
    private String departmentName;
    @NotNull(message = "Department short code can not be empty")
    private String departmentShortCode;
    @NotNull(message = "Pontoon division id can not be empty")
    private long pontoonDivisionId;
}
