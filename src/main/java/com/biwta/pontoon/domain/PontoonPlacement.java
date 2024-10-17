package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Entity
@Table
@Data
public class PontoonPlacement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Pontoon pontoon;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Ghat ghat;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Route route;

    private String remarks;

    private Float latitude;

    private Float longitude;

    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Employee> employee;
}
