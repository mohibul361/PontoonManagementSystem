package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ৭/১২/২৩
 */
@Data
public class EmployeeList {
    private Long id;
    private String pmsId;
    private String employeeName;
    private String designation;
    private String phoneNumber;
    private String email;
    private String nid;
    private Boolean status;
    private String authority;
    private String department;
    private String section;
    private String joiningDate;
    private String employeeImage;
}
