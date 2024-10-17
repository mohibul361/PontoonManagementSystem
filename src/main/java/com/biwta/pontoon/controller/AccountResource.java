package com.biwta.pontoon.controller;

import com.biwta.pontoon.controller.vm.KeyAndPasswordVM;
import com.biwta.pontoon.controller.vm.ManagedUserVM;
import com.biwta.pontoon.domain.Users;
import com.biwta.pontoon.dto.PasswordChangeDTO;
import com.biwta.pontoon.service.MailService;
import com.biwta.pontoon.service.UserService;
import com.biwta.pontoon.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AccountResource{
	
	@Autowired
    private UserService userService;
	@Autowired
    private MailService mailService;

    @PostMapping("/sign-in")
    public String login(
        @RequestParam String username,
        @RequestParam String password) {
        return userService.signIn(username, password);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        
    	if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password.");
        }
        
    	Users user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        ///mailService.sendActivationEmail(user);
    }

    @PostMapping(path = "/account/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password.");
        }

        if (userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword())) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to change password");
        }
    }


    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<Users> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user was found for this activation key.");
        }
    }

    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestParam String mail) {
        Optional<Users> user = userService.requestPasswordReset(mail);
        
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
            throw new ResponseStatusException(HttpStatus.OK, "Password sent to your email. Please check your email");
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            // log.warn("Password reset requested for non existing mail");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset requested for non existing mail");
        }
    }

    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password.");
        }
        
        Optional<Users> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
        
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user was found for this reset key.");
        }else {
            throw new ResponseStatusException(HttpStatus.OK, "Password reset successful");
        }
    }

    @GetMapping("/getAuthorites")
    public List<String> getAuthorites() {
    	return userService.getAuthorities();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
                password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
                password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @GetMapping("/profileDetails")
    public ResponseEntity<?> getAllEmployeeDetails() {
        String username = EntityUtils.getUserName();
        return ResponseEntity.ok(userService.getAllEmployeeDetails(username));
    }
    
}
