package com.biwta.pontoon.dto;

/**
 * @author nasimkabir
 * ১০/১২/২৩
 */
public interface DashboardData {
    Long getTotalPontoon();
    Long getTotalOperational();
    Long getTotalNeedRepair();
    Long getTotalSunkin();
    Long getTotalEmployee();

    Long getTotalIdle();
}
