package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the DIVISION database table.
 */
@Entity
@NamedQuery(name = "Division.findAll", query = "SELECT d FROM Division d")
public class Division implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DIVISION_ID_GENERATOR", sequenceName = "DIVISION_GENERATOR", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIVISION_ID_GENERATOR")
    private long id;

    @Column(name = "DIVISION_NAME")
    private String divisionName;

    private Boolean isActive;

    @OneToMany(mappedBy = "division", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<District> districts;

    public Division() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDivisionName() {
        return this.divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public List<District> getDistricts() {
        return this.districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public District addDistrict(District district) {
        getDistricts().add(district);
        district.setDivision(this);

        return district;
    }

    public District removeDistrict(District district) {
        getDistricts().remove(district);
        district.setDivision(null);

        return district;
    }

}