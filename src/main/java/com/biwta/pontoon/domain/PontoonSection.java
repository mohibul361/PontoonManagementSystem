package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Entity
@Table(name = "pontoon_section")
@Data
public class PontoonSection extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(
            name = "pontoon_section_sequence",
            sequenceName = "pontoon_section_sequence",
            allocationSize = 1000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pontoon_section_sequence"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "pontoon_division_id")
    private PontoonDivision pontoonDivision;

    @Column(nullable = false)
    private Boolean hasDepartment;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private PontoonDepartment pontoonDepartment;
    @Column(name = "is_assigned")
    private Boolean isAssigned;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "division", nullable = false)
    private Division division;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "district", nullable = false)
    private District district;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "thana_id", nullable = false)
    private Thana thana;
}
