package com.biwta.pontoon.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@Data
@Builder
public class DistrictListDto {
    private Long id;
    private String districtName;
}
