package com.biwta.pontoon.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nasimkabir
 * ৩১/১/২৪
 */
@Data
public class PontoonDetailsModel {
    private String pontoonId;
    private String pontoonType;
    private String pontoonStatus;
    private String buildYear;
    private String buildCost;
    private String budgetSource;
    private String procuringEntity;
    private String manufacturedBy;
    private Float latitude;
    private Float longitude;
    private String ghatName;
    private String sectionName;
    private String routeName;
    private String pontoonImage;
    private List<LoshkarDetailsModel> loshkarDetails = new ArrayList<>();
}
