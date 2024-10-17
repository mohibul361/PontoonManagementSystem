package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
@Table(name = "pontoon_department")
public class PontoonDepartment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(
            name = "pontoon_department_sequence",
            sequenceName = "pontoon_department_sequence",
            allocationSize = 100
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pontoon_department_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String departmentName;

    @Column(unique = true, nullable = false)
    private String departmentShortCode;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "division_id", nullable = false)
    private PontoonDivision division;

    private Boolean isActive;
    @Column(name = "is_assinged")
    private Boolean isAssigned;
}
