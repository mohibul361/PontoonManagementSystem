package com.biwta.pontoon.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@Data
public class AddLoshkorTransferDto {
    private long loshkorId;
    private List<Long> pontoonId;
    private Date transferDate;
}
