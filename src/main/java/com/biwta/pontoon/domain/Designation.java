package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @author nasimkabir
 * ১৮/১২/২৩
 */
@Entity
@Table
@Data
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "is_active")
    private boolean isActive;
    @JsonIgnore
    @OneToOne
    private PontoonDepartment department;
}
