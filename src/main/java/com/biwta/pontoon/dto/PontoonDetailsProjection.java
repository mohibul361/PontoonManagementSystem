package com.biwta.pontoon.dto;

/**
 * @author nasimkabir
 * ৩১/১/২৪
 */
public interface PontoonDetailsProjection {
    String getPontoonId();
    String getPontoonType();
    String getPontoonStatus();
    String getBuildYear();
    String getBuildCost();
    String getBudgetSource();
    String getProcuringEntity();
    String getManufacturedBy();
    Float getLatitude();
    Float getLongitude();
    String getGhatName();
    String getSectionName();
    String getRouteName();
    String getPontoonImage();
}
