package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
@Data
public class PontoonTransferList {
    private Long id;
    private String pontoonName;
    private String sectionName;
    private String ghatName;
    private String transferDate;
    private Float longitude;
    private Float latitude;
}
