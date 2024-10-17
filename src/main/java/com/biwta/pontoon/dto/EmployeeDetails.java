package com.biwta.pontoon.dto;

import java.util.Date;

/**
 * @author nasimkabir
 * ১৪/১/২৪
 */
public interface EmployeeDetails {
    Long getPontoonId();
    String getPontoonName();
    Long getGhatId();
    String getGhatName();
    Long getSectionId();
    String getSectionName();
    Long getEmployeeId();
    String getEmployeeName();
    Float getLatitude();
    Float getLongitude();
    Date getTransferDate();
    Date getTillDate();
}
