package com.biwta.pontoon.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ৩০/১১/২৩
 */
@Data
public class AddPontoonTransferDto {
    private Long pontoonId;
    private Long sectionId;
    private List<Long> employeeId;
    private Long ghatId;
    private Float latitude;
    private Float longitude;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date transferDate;
    private String orderNo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
}
