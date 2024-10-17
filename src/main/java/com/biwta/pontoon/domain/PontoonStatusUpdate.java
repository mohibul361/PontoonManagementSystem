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
@Table(name = "pontoon_status_update")
public class PontoonStatusUpdate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Pontoon pontoon;

    @ManyToOne
    private PontoonStatus status;

    private String documentUrl;

    private String description;

}
