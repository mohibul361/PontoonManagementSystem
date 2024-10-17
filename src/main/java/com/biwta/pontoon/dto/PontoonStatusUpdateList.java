package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nasimkabir
 * ৫/১২/২৩
 */
@Data
@AllArgsConstructor
public class PontoonStatusUpdateList {
    private long id;
    private String pontoonId;
    private String statusName;
    private String description;
}
