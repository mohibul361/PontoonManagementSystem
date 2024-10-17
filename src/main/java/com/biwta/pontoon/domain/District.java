package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the DISTRICT database table.
 * 
 */
@Entity
@Table(name="DISTRICT")
public class District implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DISTRICT_ID_GENERATOR", sequenceName="DISTRICT_GENERATOR", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRICT_ID_GENERATOR")
	private long id;

	@Column(name="DISTRICT_NAME")
	private String districtName;

	private Boolean isActive;

	//bi-directional many-to-one association to Division
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Division division;

	public District() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDistrictName() {
		return this.districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Division getDivision() {
		return this.division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}
}