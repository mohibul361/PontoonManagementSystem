package com.biwta.pontoon.dto;

import com.biwta.pontoon.domain.Authority;
import com.biwta.pontoon.domain.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class AdminUserDTO {

    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

	private String username;

    @JsonIgnore
    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private Instant createdDate;

    @JsonIgnore
    private String lastModifiedBy;

    @JsonIgnore
    private Instant lastModifiedDate;

    private Set<String> authorities;

	private long employeeId;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

	public AdminUserDTO(Set<String> authorities) {
		this.authorities = authorities;
	}

	public AdminUserDTO(Users user) {
        this.setId(user.getId());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
		this.username = user.getUsername();
        this.setActivated(user.isActivated());
        this.setLangKey(user.getLangKey());
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

}
