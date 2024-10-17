package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String pmsId;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String employeeName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nid;

    @ManyToOne
    private PontoonDivision division;

    @ManyToOne
    private PontoonDepartment department;

    @ManyToOne
    private PontoonSection section;

    @ManyToOne
    private Authority authority;

    private String employeeRemarks;
    private String username;

    @OneToOne
    private Designation designation;

    @Temporal(TemporalType.DATE)
    @Column(name = "joining_date")
    private Date joiningDate;

    private String employeeImage;

    private String employeeSignature;
    private Boolean isActive;

}
