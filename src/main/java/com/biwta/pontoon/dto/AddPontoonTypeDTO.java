package com.biwta.pontoon.dto;

//import com.biwta.pontoon.enumuration.PontoonType;
import com.biwta.pontoon.enumuration.PontoonUnit;
import lombok.Data;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
public class AddPontoonTypeDTO {
    private String typeName;
    private String length;
    private String breadth;
    private String height;
    private PontoonUnit pontoonUnit;
    private long pontoonTypeId;
}
