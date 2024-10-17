package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteListDto {
    private Long id;
    private String routeName;
}
