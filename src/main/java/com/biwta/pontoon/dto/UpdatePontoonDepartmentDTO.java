package com.biwta.pontoon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author nasimkabir
 * ১৯/১২/২৩
 */
@Data
public class UpdatePontoonDepartmentDTO {
    @NotNull(message = "Department name can not be empty")
    private String departmentName;
    @NotNull(message = "Department short code can not be empty")
    private String departmentShortCode;
}
