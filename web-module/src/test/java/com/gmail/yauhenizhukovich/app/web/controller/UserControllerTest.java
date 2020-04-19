package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauhenizhukovich.app.web.controller.UserController.UPDATE_PASSWORD_MESSAGE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(roles = "ADMINISTRATOR")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void loginPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/login")).andExpect(status().isOk());
    }

    @Test
    void getAllUsers_returnRedirectedUrl() throws Exception {
        mockMvc.perform(get("/users")).
                andExpect(redirectedUrl("/users/page/1"));
    }

    @Test
    void getUsersByPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/page/13")).
                andExpect(status().isOk());
    }

    @Test
    void getUsersByPage_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/users/page/13")).
                andExpect(status().isOk());
        verify(userService, times(1)).getUsersByPage(eq(13));
    }

    @Test
    void addUserPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/add")).andExpect(status().isOk());
    }

    @Test
    void addUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().isOk());
    }

    @Test
    void updateUserRole_returnRedirectedUrl() throws Exception {
        mockMvc.perform(
                post("/users/25/update/role")
                        .param("userRole", "SALE_USER")
                        .param("pageNumber", "2")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users/page/2"));
    }

    @Test
    void updateUserRole_callBusinessLogic() throws Exception, AdministratorChangingException {
        mockMvc.perform(
                post("/users/test/update/role")
                        .param("userRole", "SALE_USER")
                        .param("pageNumber", "2")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users/page/2"));
        verify(userService, times(1)).updateRoleByUniqueNumber(eq(RoleEnum.SALE_USER), eq("test"));
    }

    @Test
    void updateUserPassword_returnRedirectedUrl() throws Exception {
        mockMvc.perform(
                get("/users/44/update/password")
                        .param("uniqueNumber", "test")
                        .param("pageNumber", "1")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users/page/1?message=" + UPDATE_PASSWORD_MESSAGE));
    }

    @Test
    void updateUserPassword_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/users/test/update/password")
                        .param("uniqueNumber", "test")
                        .param("pageNumber", "1")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users/page/1?message=" + UPDATE_PASSWORD_MESSAGE));
        verify(userService, times(1)).updatePasswordByUniqueNumber(eq("test"));
    }

    @Test
    void deleteUsers_returnRedirectedUrl() throws Exception {
        mockMvc.perform(
                post("/users/delete")
                        .param("pageNumber", "5")
                        .param("uniqueNumbers", "test1")
                        .param("uniqueNumbers", "test2")
                        .param("uniqueNumbers", "test3")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users"));
    }

    @Test
    void deleteUsers_callBusinessLogic() throws Exception, AdministratorChangingException {
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add("test1");
        uniqueNumbers.add("test2");
        uniqueNumbers.add("test3");
        mockMvc.perform(
                post("/users/delete")
                        .param("pageNumber", "5")
                        .param("uniqueNumbers", "test1")
                        .param("uniqueNumbers", "test2")
                        .param("uniqueNumbers", "test3")
                        .contentType(MediaType.ALL_VALUE)
        ).andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).deleteUsersByUniqueNumber(eq(uniqueNumbers));
    }

}