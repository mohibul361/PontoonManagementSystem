package com.biwta.pontoon.domain;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pontoon_status_update_history")
public class PontoonStatusUpdateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private PontoonStatus status;

    @OneToOne
    private Pontoon pontoon;

    private String documentUrl;

    private String description;

    @Column(unique = true, nullable = false)
    private Date effectiveDate;

    @Column(unique = true, nullable = false)
    private Date tillDate;
}
