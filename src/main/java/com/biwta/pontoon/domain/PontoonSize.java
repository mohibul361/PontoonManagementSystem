package com.biwta.pontoon.domain;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */

import com.biwta.pontoon.enumuration.PontoonUnit;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "pontoon_size")
public class PontoonSize extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sizeName;

    @Column( nullable = false,length = 20)
    private String length;

    @Column( nullable = false,length = 20)
    private String breadth;

    @Column( nullable = false,length = 20)
    private String height;

    @Column( nullable = false)
    private Boolean isActive;

    @Column( nullable = false)
    private PontoonUnit pontoonUnit;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pontoon_type_id", nullable = false)
    private PontoonType pontoonType;
}
