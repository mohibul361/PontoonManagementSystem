package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteDTO {

    private Long id;

    private String routeName;

    private Boolean isActive;
}
