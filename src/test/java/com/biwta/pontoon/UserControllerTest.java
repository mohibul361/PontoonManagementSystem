/*
package com.biwta.pontoon;

import com.biwta.pontoon.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.biwta.pontoon.controller.vm.KeyAndPasswordVM;
import com.biwta.pontoon.controller.vm.ManagedUserVM;
import com.biwta.pontoon.domain.Authority;
import com.biwta.pontoon.domain.Users;
import com.biwta.pontoon.dto.PasswordChangeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@localhost", password = "admin", roles = "ADMIN")
public class UserControllerTest {

    @Autowired
    private MockMvc userMockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    ManagedUserVM managedUserVM = new ManagedUserVM();
    Users users = new Users();
    PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
    KeyAndPasswordVM keyAndPasswordVM = new KeyAndPasswordVM();


    @BeforeEach
    void setup(){
        Set<String> authority = new HashSet<>();
        authority.add("ADMIN");
        Authority authority1 = new Authority();
        authority1.setName("ADMIN");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority1);

        managedUserVM.setPassword("123456");
        managedUserVM.setFirstName("Mehedi");
        managedUserVM.setLastName("Hasan");
        managedUserVM.setEmail("mehedi@mehedi");
        managedUserVM.setLangKey("en");
        managedUserVM.setAuthorities(authority);

        users.setId(1l);
        users.setPassword("123456");
        users.setFirstName("Mehedi");
        users.setLastName("Hasan");
        users.setEmail("mehedi@mehedi");
        users.setLangKey("en");
        users.setAuthorities(authorities);

        passwordChangeDTO.setCurrentPassword("65432");
        passwordChangeDTO.setNewPassword("12345");

        keyAndPasswordVM.setKey("1234567");
        keyAndPasswordVM.setNewPassword("123456");
    }


    @Test
    void login() throws Exception {
        when(userService.signIn("admin@localhost", "admin")).thenReturn("jwt-token");
        userMockMvc.perform(post("/api/sign-in").contentType(MediaType.APPLICATION_JSON).param("username", "admin@localhost").param("password", "admin"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void registration() throws Exception {
        when(userService.registerUser(managedUserVM, managedUserVM.getPassword())).thenReturn(users);
        userMockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(managedUserVM)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void resetPasswordInit() throws Exception {
        userMockMvc.perform(post("/api/account/reset-password/init").contentType(MediaType.APPLICATION_JSON)
            .content("local@local"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void resetPasswordFinish() throws Exception {

        Optional<Users> user = Optional.of(users);
        when(userService.completePasswordReset("123456", "1234567")).thenReturn(user);
        userMockMvc.perform(post("/api/account/reset-password/finish").
            contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(keyAndPasswordVM)))
            .andExpect(status().isOk())
            .andDo(print());
    }


    @Test
    void passwordChange() throws Exception {
        userMockMvc.perform(post("/api/account/change-password").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordChangeDTO)))
            .andExpect(status().isOk())
            .andDo(print());
    }




}
*/
