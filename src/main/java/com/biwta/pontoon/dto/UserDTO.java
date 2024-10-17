package com.biwta.pontoon.dto;
import com.biwta.pontoon.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String login;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(Users user) {
        this.setId(user.getId());
        this.setLogin(user.getEmail());
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
    
}
