package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the THANA database table.
 */
@Entity
@NamedQuery(name = "Thana.findAll", query = "SELECT t FROM Thana t")
public class Thana implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "THANA_ID_GENERATOR", sequenceName = "THANA_GENERATOR", initialValue = 520, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THANA_ID_GENERATOR")
    private long id;

    @Column(name = "THANA_NAME")
    private String thanaName;

    //bi-directional many-to-one association to District
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_transactions__product"))
    private District district;

    public Thana() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThanaName() {
        return this.thanaName;
    }

    public void setThanaName(String thanaName) {
        this.thanaName = thanaName;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

}