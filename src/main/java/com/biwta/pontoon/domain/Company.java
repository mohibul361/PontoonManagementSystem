package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Entity
@Table
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String bin;
    private String address;
    private Boolean isActive;

}
