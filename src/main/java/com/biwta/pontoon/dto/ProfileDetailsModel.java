package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ১৮/১/২৪
 */
@Data
public class ProfileDetailsModel {
    private Long id;
    private String pmsId;
    private String employeeName;
    private String designation;
    private String phoneNumber;
    private String email;
    private String nid;
    private Boolean status;
    private String authority;
    private String joiningDate;
    private String employeeImage;
}
