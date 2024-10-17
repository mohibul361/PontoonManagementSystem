package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
@Table(name = "pontoon")
public class Pontoon extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "ponton_type_id", foreignKey = @ForeignKey(name = "fk_pontoon__pontoon_type"))
    private PontoonSize pontoonSize;
    @Column(name = "pontoon_id", nullable = false, unique = true)
    private String pontoonId;
    private Integer buildYear;
    private String manufacturedBy;
    private BigDecimal buildCost;
    @ManyToOne
    @JoinColumn(name = "procuring_entity")
    private ProcuringEntry procuringEntity;
    private String budgetSource;
    private String remarks;
    @Column(nullable = false)
    private Boolean isActive;
    @OneToMany(mappedBy = "pontoon", fetch = FetchType.EAGER)
    private List<PontoonImage> pontoonImage;
    @Column(nullable = false)
    private Boolean isAssigned;
    @Temporal(TemporalType.DATE)
    @Column(name = "receiving_date")
    private Date receivingDate;
}
