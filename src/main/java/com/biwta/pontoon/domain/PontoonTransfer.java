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
@Entity
@Table
@Data
public class PontoonTransfer extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Pontoon pontoon;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PontoonSection section;

    @ManyToMany
    @JoinTable(
            name = "pontoon_transfer_employee",
            joinColumns = @JoinColumn(name = "pontoon_transfer_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employee;

    @OneToOne
    @JoinColumn(nullable = false)
    private Ghat ghat;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column(nullable = false)
    private Date transferDate;

    private Date tillDate;

    @Column(name="order_no")
    private String orderNo;
    @Temporal(TemporalType.DATE)
    @Column(name="order_date")
    private Date orderDate;

    @Column(name="documents_file")
    private String documentsFile;
}
