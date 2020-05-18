package com.gmail.yauhenizhukovich.app.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.NOT_EMPTY_FIRSTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_EMAIL;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PATRONYMIC;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ROLE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAPIController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void addUser_returnStatusCreated() throws Exception {
        AddUserDTO addUser = getAddUser();
        String content = objectMapper.writeValueAsString(addUser);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void addUser_callBusinessLogic() throws Exception, UserExistenceException {
        AddUserDTO addUser = getAddUser();
        String content = objectMapper.writeValueAsString(addUser);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
        ArgumentCaptor<AddUserDTO> userCaptor = ArgumentCaptor.forClass(AddUserDTO.class);
        verify(userService, times(1)).addUser(userCaptor.capture());
    }

    @Test
    void addUser_returnUserWithValidFields() throws Exception, UserExistenceException {
        AddUserDTO addUser = getAddUser();
        String content = objectMapper.writeValueAsString(addUser);
        UserDTO returnedUser = getUser();
        when(userService.addUser(addUser)).thenReturn(returnedUser);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isCreated());
        ArgumentCaptor<AddUserDTO> userCaptor = ArgumentCaptor.forClass(AddUserDTO.class);
        verify(userService, times(1)).addUser(userCaptor.capture());
        Assertions.assertThat(userCaptor.getValue().getEmail()).isEqualTo(addUser.getEmail());
        Assertions.assertThat(userCaptor.getValue().getRole()).isEqualTo(addUser.getRole());
        Assertions.assertThat(userCaptor.getValue().getLastName()).isEqualTo(addUser.getLastName());
    }

    private UserDTO getUser() {
        UserDTO user = new UserDTO();
        user.setEmail(VALID_EMAIL);
        user.setRole(VALID_ROLE);
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setPatronymic(VALID_PATRONYMIC);
        return user;
    }

    @Test
    void addUser_returnUser() throws Exception, UserExistenceException {
        AddUserDTO addUser = getAddUser();
        String content = objectMapper.writeValueAsString(addUser);
        UserDTO returnedUser = getUser();
        when(userService.addUser(eq(addUser))).thenReturn(returnedUser);
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andReturn();
        String expectedContent = objectMapper.writeValueAsString(returnedUser);
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(expectedContent).isEqualTo(actualContent);
    }

    @Test
    void addInvalidUser_returnErrors() throws Exception, UserExistenceException {
        AddUserDTO addUser = new AddUserDTO();
        addUser.setEmail("###");
        addUser.setFirstName("");
        String content = objectMapper.writeValueAsString(addUser);
        UserDTO returnedUser = getUser();
        when(userService.addUser(eq(addUser))).thenReturn(returnedUser);
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(EMAIL_PATTERN_MESSAGE);
        Assertions.assertThat(actualContent).contains(NOT_EMPTY_FIRSTNAME_MESSAGE);
    }

    private AddUserDTO getAddUser() {
        AddUserDTO addUser = new AddUserDTO();
        addUser.setEmail(VALID_EMAIL);
        addUser.setRole(VALID_ROLE);
        addUser.setFirstName(VALID_FIRST_NAME);
        addUser.setLastName(VALID_LAST_NAME);
        addUser.setPatronymic(VALID_PATRONYMIC);
        return addUser;
    }

}