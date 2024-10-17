package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {

    private String currentPassword;
    private String newPassword;
	
    public String getCurrentPassword() {
		return currentPassword;
	}
	
    public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	
    public String getNewPassword() {
		return newPassword;
	}
	
    public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
