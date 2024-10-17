package com.biwta.pontoon.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddEmployeeDTO {
    @NotNull(message = "PMS ID can not be null")
    private String pmsId;
    private String email;
    @NotNull(message = "Employee name can not be null")
    private String employeeName;
    @NotNull(message = "Phone number can not be null")
    private String phoneNumber;
    private String nid;
    @NotNull(message = "Division ID can not be null")
    private Long divisionId;
    @NotNull(message = "Department ID can not be null")
    private Long departmentId;
    @NotNull(message = "Section ID can not be null")
    private Long sectionId;
    @NotNull(message = "Authority ID can not be null")
    private String authorityId;
    private String employeeRemarks;
    private long designationId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;
    private String password;
    private String username;
}
