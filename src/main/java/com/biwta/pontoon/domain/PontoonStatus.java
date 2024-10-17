package com.biwta.pontoon.domain;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "pontoon_status")
public class PontoonStatus extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String statusName;
    private Boolean isActive;
}
