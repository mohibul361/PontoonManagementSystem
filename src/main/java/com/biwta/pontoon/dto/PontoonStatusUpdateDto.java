package com.biwta.pontoon.dto;

import lombok.Data;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Data
public class PontoonStatusUpdateDto {
    private long pontoonId;
    private long statusId;
    private String description;
}
