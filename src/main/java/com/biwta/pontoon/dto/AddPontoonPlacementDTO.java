package com.biwta.pontoon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@Data
public class AddPontoonPlacementDTO {
    @NotNull(message = "Pontoon Id Can't be Null")
    private long pontoonId;
    @NotNull(message = "Pontoon Id Can't be Null")
    private long ghatId;
    @NotNull(message = "Route Id Can't be Null")
    private long routeId;
    private String remarks;
    @NotNull(message = "Latitude Can't be Null")
    private Float latitude;
    @NotNull(message = "Longitude Can't be Null")
    private Float longitude;
    private List<Long> employees;
    @NotNull(message = "Division Id Can't be Null")
    private long divisionId;
    @NotNull(message = "District Id Can't be Null")
    private long districtId;

    public AddPontoonPlacementDTO(long pontoonId, long ghatId, long routeId,float latitude,float longitude) {
        this.pontoonId = pontoonId;
        this.ghatId = ghatId;
        this.routeId = routeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
