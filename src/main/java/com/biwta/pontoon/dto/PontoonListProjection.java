package com.biwta.pontoon.dto;

import java.math.BigDecimal;

/**
 * @author nasimkabir
 * ৭/১২/২৩
 */
public interface PontoonListProjection {
      Long getId();
      String getPontoonId();
     String getPontoonType();
     Integer getBuildYear();
     String getManufacturedBy();
     BigDecimal getBuildCost();
     String getProcuringEntity();
     String getBudgetSource();
     String getRemarks();
     Boolean getIsAssigned();
     String getPontoonStatusName();

     String getPontoonImage();
}
