package com.biwta.pontoon.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@Data
public class UpdateEmployeeDTO {
    @NotNull(message = "Employee name can not be null")
    Long loskharId;
    @NotNull(message = "Employee name can not be null")
    private String employeeName;
    private String username;
    @NotNull(message = "Phone number can not be null")
    private String phoneNumber;
    @NotNull(message = "PMS ID can not be null")
    private String pmsId;
    private String email;
    private String nid;
    @NotNull(message = "Division ID can not be null")
    private Long divisionId;
    @NotNull(message = "Department ID can not be null")
    private Long departmentId;
    @NotNull(message = "Section ID can not be null")
    private Long sectionId;
    /*@NotNull(message = "Authority ID can not be null")
    private String authorityId;*/
    private String employeeRemarks;
    private long designationId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;
    private String password;
}
