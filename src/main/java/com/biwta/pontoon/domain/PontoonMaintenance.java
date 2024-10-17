package com.biwta.pontoon.domain;

import com.biwta.pontoon.enumuration.BudgetSource;
import com.biwta.pontoon.enumuration.MaintenanceType;
import com.biwta.pontoon.enumuration.Tendering;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
public class PontoonMaintenance extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Pontoon pontoon;
    @Column(nullable = false)
    private MaintenanceType maintenanceType;
    private Date repairDateRange;
    private Double repairCost;
    // company table asba
    @OneToOne
    private Company repairedBy;
    private String repairDescription;
    private Boolean isActive;
    private Integer financialYear;
    private BudgetSource budgetSource;
    @ManyToOne
    @JoinColumn(name = "procuring_entity")
    private ProcuringEntry procuringEntity;
    private Tendering tenderingMethod;
    private String documentUrl;
    private String approvalDocumentUrl;
    private String ltmDocumentUrl;
    @OneToOne
    private PontoonDepartment department;
}
