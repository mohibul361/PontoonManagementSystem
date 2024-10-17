package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Entity
@Table
@Data
public class PontoonDivision extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(
            name = "pontoon_division_sequence",
            sequenceName = "pontoon_division_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pontoon_division_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String divisionName;
    private Boolean isActive;
    @Column(name = "is_assigned")
    private Boolean isAssigned;
}
