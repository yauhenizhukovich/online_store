package com.gmail.yauhenizhukovich.app.web.controller.integration;

import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauhenizhukovich.app.web.controller.UserController.UPDATE_USER_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "ADMINISTRATOR")
public class UserControllerITTest {

    public static final String VALID_NAME = "name";
    private static final String VALID_EMAIL = "test@gmail.com";
    public static final RoleEnumService VALID_ROLE = RoleEnumService.CUSTOMER_USER;
    public static final String VALID_UNIQUE_NUMBER = "2df2f43a-c873-48d9-802d-2a06d60e026b";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsers_returnUsers() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("admin@gmail.com")))
                .andExpect(content().string(containsString("Ivanov")));
    }

    @Test
    public void getAddUserPage_returnAddUserPage() throws Exception {
        mockMvc.perform(get("/users/add")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("User_model")));
    }

    @Test
    public void addUser_returnAddedUser() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
                .param("firstName", VALID_NAME)
                .param("lastName", VALID_NAME)
                .param("patronymic", VALID_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(VALID_EMAIL)))
                .andExpect(content().string(containsString(VALID_ROLE.name())))
                .andExpect(content().string(containsString(VALID_NAME)));
    }

    @Test
    public void getUser_returnUser() throws Exception {
        mockMvc.perform(get("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Ivanovich")))
                .andExpect(content().string(containsString("admin@gmail.com")));
    }

    @Test
    public void deleteUsers_redirectToUsersPage() throws Exception {
        mockMvc.perform(post("/users/delete")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    public void updateUser_returnUserPage() throws Exception {
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .contentType(MediaType.TEXT_HTML)
                .param("updatePassword", "true")
                .param("role", RoleEnumService.ADMINISTRATOR.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(UPDATE_USER_MESSAGE)));
    }

}
