package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An authority (a security role) used by Spring Security.
 */

@Entity
@Table(name = "authority")
@Data
public class Authority {
    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
