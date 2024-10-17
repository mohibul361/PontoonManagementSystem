package com.biwta.pontoon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddGhatDTO {
    @NotNull(message = "Ghat Name Can't be Null")
    private String ghatName;
    @NotNull(message = "Section Id Can't be Null")
    private long sectionId;
    @NotNull(message = "Route Id Can't be Null")
    private long routeId;
    @NotNull(message = "Latitude Can't be Null")
    private Float latitude;
    @NotNull(message = "Longitude Can't be Null")
    private Float longitude;
    @NotNull(message = "Division Id Can't be Null")
    private long divisionId;
    @NotNull(message = "District Id Can't be Null")
    private long districtId;
    @NotNull(message = "Thana Id Can't be Null")
    private long thanaId;
    @NotNull(message = "Port Id Can't be Null")
    private long portId;
}
