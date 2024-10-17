package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
public class LoshkorTransfer extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Employee employee;


    @ManyToMany
    @JoinTable(
            name = "loshkar_transfer_pontoon",
            joinColumns = @JoinColumn(name = "loshkar_transfer_id"),
            inverseJoinColumns = @JoinColumn(name = "pontoon_id")
    )
    private List<Pontoon> pontoon;
    private Date addDate;
    private Date effectiveDate;
    private Date tillDate;
}
