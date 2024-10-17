package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Data
public class PontoonPlacementListDto {
    private Long id;
    private long pontoonId;
    private String pontoonName;
    private long ghatId;
    private String ghatName;
    private long routeId;
    private String routeName;
    private String remarks;
    private Float latitude;
    private Float longitude;
    private long employeeId;
    private String employeeName;
}
