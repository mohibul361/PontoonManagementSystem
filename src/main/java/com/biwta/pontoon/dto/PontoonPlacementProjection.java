package com.biwta.pontoon.dto;

import java.math.BigDecimal;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
public interface PontoonPlacementProjection {
    Long getId();

    String getPontoonType();

    String getPontoonId();

    String getGhatName();

    String getSectionName();

    String getRouteName();

    Integer getBuildYear();

    String getManufacturedBy();

    BigDecimal getBuildCost();

    String getProcuringEntity();

    String getBudgetSource();

    String getRemarks();

    Boolean getIsAssigned();

    String getStatus();
}
