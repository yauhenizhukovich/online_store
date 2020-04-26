package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(roles = "ADMINISTRATOR")
class UserControllerTest {

    private static final int PAGE = 3;
    private static final String VALID_FIRST_NAME = "Ivan";
    private static final String VALID_LAST_NAME = "Sidorov";
    private static final String VALID_PATRONYMIC = "Petrovich";
    private static final String VALID_EMAIL = "sidorov@gmail.com";
    private static final String VALID_UNIQUE_NUMBER = "testUniqueNumber";
    private static final RoleEnumService VALID_ROLE = RoleEnumService.SALE_USER;
    private static final String VALID_TELEPHONE = "80292258425";
    private static final String VALID_ADDRESS = "Beverly Hills, 25A";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void loginPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void getAllUsers_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersWithParameters_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersWithInvalidParameters_returnBadRequest() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", "d"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersWithParameters_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
        verify(userService, times(1)).getPages();
        verify(userService, times(1)).getUsersByPage(eq(PAGE));
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightEmail() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        String email = user.getEmail();
        Assertions.assertThat(actualContent).contains(email);
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightFirstName() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        String firstName = user.getFirstName();
        Assertions.assertThat(actualContent).contains(firstName);
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightLastName() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        String lastName = user.getLastName();
        Assertions.assertThat(actualContent).contains(lastName);
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightPatronymic() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        String patronymic = user.getPatronymic();
        Assertions.assertThat(actualContent).contains(patronymic);
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightRole() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        RoleEnumService role = user.getRole();
        Assertions.assertThat(actualContent).contains(role.name());
    }

    @Test
    void getAllUsersWithParameters_returnUsersWithRightUniqueNumber() throws Exception {
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        UserDTO user = users.get(0);
        String uniqueNumber = user.getUniqueNumber();
        Assertions.assertThat(actualContent).contains(uniqueNumber);
    }

    @Test
    void getAllUsersWithParameters_returnUsersPageWithRightPages() throws Exception {
        List<Integer> pages = getPages(5);
        List<UserDTO> users = getOneUserList();
        when(userService.getUsersByPage(PAGE)).thenReturn(users);
        when(userService.getPages()).thenReturn(pages);
        MvcResult result = mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Collections.shuffle(pages);
        Assertions.assertThat(actualContent).contains("page" + pages.get(0));
    }

    @Test
    void getAddUserPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/add")).andExpect(status().isOk());
    }

    @Test
    void getAddUserPage_returnAddUserPageWithUserAndRoles() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/add")).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(RoleEnumService.SECURE_API_USER.name());
        Assertions.assertThat(actualContent).contains("User_model");
    }

    @Test
    void addValidUser_redirectOnUsersUrl() throws Exception, UserExistenceException {
        UserDTO returnedUser = getFilledUser();
        when(userService.addUser(any())).thenReturn(returnedUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addInvalidUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithEmptyFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", "")
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithSmallFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", "a")
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithLongFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", generateStringByLength(21))
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithUnconventionalFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", "test1")
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithEmptyLastName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", "")
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithSmallLastName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", "a")
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithLongLastName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", generateStringByLength(41))
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithUnconventionalLastName_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", "test1")
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithEmptyPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", "")
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithSmallPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", "a")
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithLongPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", generateStringByLength(41))
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithUnconventionalPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", "test1")
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithEmptyEmail_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", "")
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithUnconventionalEmail_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", "test")
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
    }

    @Test
    void addUserWithNullRole_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", (String) null)
        ).andExpect(status().isOk());
    }

    @Test
    void addValidUser_callBusinessLogic() throws Exception, UserExistenceException {
        UserDTO user = getUserToAdd();
        UserDTO returnedUser = getFilledUser();
        when(userService.addUser(user)).thenReturn(returnedUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("patronymic", user.getPatronymic())
                .param("email", user.getEmail())
                .param("role", user.getRole().name())
                .param("address", user.getAddress())
                .param("telephone", user.getTelephone())
        ).andExpect(status().isOk());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void addInvalidUser_returnAddUserPage() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_ROLE.name());
        Assertions.assertThat(actualContent).contains("User_model");
    }

    @Test
    void addUserWithInvalidFirstName_returnAddFilledUserPage() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "test1")
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", "")
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_LAST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_EMAIL);
    }

    @Test
    void addUserWithUnconventionalEmail_returnAddUserPageWithError() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", "test")
                .param("role", VALID_ROLE.name())
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(EMAIL_PATTERN_MESSAGE);
    }

    @Test
    void addValidUser_returnUserPage() throws Exception, UserExistenceException {
        UserDTO user = getFilledUser();
        when(userService.addUser(any())).thenReturn(user);
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualContent).contains(VALID_LAST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_EMAIL);
    }

    @Test
    void getUserPage_returnStatusOk() throws Exception {
        UserDTO user = getFilledUser();
        when(userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(get("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER))
                .andExpect(status().isOk());
    }

    @Test
    void getUserPageByUniqueNumber_callBusinessLogic() throws Exception {
        UserDTO user = getFilledUser();
        when(userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(get("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER))
                .andExpect(status().isOk());
        verify(userService, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
    }

    @Test
    void getUserPageByUniqueNumber_returnUserWithValidFullName() throws Exception {
        UserDTO user = getFilledUser();
        when(userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_FIRST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_LAST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_PATRONYMIC);
    }

    @Test
    void getUserPageByUniqueNumber_returnUserWithValidEmailAndRole() throws Exception {
        UserDTO user = getFilledUser();
        when(userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_EMAIL);
        Assertions.assertThat(actualContent).contains(VALID_ROLE.name());
    }

    @Test
    void updateUser_returnRedirectedUrl() throws Exception, AdministratorChangingException {
        UserDTO user = getFilledUser();
        when(userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("role", VALID_ROLE.name()))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserSetInvalidRole_returnBadRequest() throws Exception {
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("role", "test")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_callUpdatePasswordAndRole() throws Exception, AdministratorChangingException {
        UserDTO user = getFilledUser();
        when(userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER)).thenReturn(user);
        when(userService.updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("role", VALID_ROLE.name())
                .param("destination", "test")
        ).andExpect(status().isOk());
        verify(userService, times(1)).updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userService, times(1)).updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER);
    }

    @Test
    void updateUser_callUpdatePassword() throws Exception, AdministratorChangingException {
        UserDTO user = getFilledUser();
        when(userService.updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("destination", "test")
        ).andExpect(status().isOk());
        verify(userService, times(1)).updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userService, times(0)).updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER);
    }

    @Test
    void updateUser_callUpdateRole() throws Exception, AdministratorChangingException {
        UserDTO user = getFilledUser();
        when(userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER)).thenReturn(user);
        mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("role", VALID_ROLE.name())
        ).andExpect(status().isOk());
        verify(userService, times(0)).updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userService, times(1)).updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER);
    }

    @Test
    void tryToUpdateRoleOfLastAdministrator_returnUserPageWithError() throws Exception, AdministratorChangingException {
        UserDTO user = getFilledUser();
        when(userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(user);
        when(userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER))
                .thenThrow(new AdministratorChangingException("You should have at least one administrator!"));
        MvcResult result = mockMvc.perform(post("/users/{uniqueNumber}", VALID_UNIQUE_NUMBER)
                .param("role", VALID_ROLE.name())
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains("You should have at least one administrator!");
    }

    @Test
    void deleteUsers_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users/delete")
                .param("uniqueNumbers", "test1")
                .param("uniqueNumbers", "test2")
        ).andExpect(redirectedUrl("/users"));
    }

    @Test
    void dontDeleteUsers_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users/delete"))
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void deleteUsers_callBusinessLogic() throws Exception, AdministratorChangingException {
        mockMvc.perform(post("/users/delete")
                .param("uniqueNumbers", VALID_UNIQUE_NUMBER)
                .param("uniqueNumbers", "test1")
        ).andExpect(redirectedUrl("/users"));
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        uniqueNumbers.add("test1");
        verify(userService, times(1)).deleteUsersByUniqueNumber(uniqueNumbers);
    }

    @Test
    void tryToDeleteLastAdministrator_returnUsersPageWithError() throws Exception, AdministratorChangingException {
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        uniqueNumbers.add("test1");
        when(userService.deleteUsersByUniqueNumber(uniqueNumbers)).thenThrow(new AdministratorChangingException(
                "You can't delete all administrators!"));
        mockMvc.perform(post("/users/delete")
                .param("uniqueNumbers", VALID_UNIQUE_NUMBER)
                .param("uniqueNumbers", "test1")
        ).andExpect(redirectedUrl("/users?message=" + "You can't delete all administrators!"));
    }

    @Test
    void getProfile_returnStatusOk() throws Exception, AnonymousUserException {
        UserDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(get("/users/profile")).andExpect(status().isOk());
    }

    @Test
    void getProfile_callBusinessLogic() throws Exception, AnonymousUserException {
        UserDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(get("/users/profile")).andExpect(status().isOk());
        verify(userService, times(1)).getUserProfile();
    }

    @Test
    void getProfile_returnUserForProfile() throws Exception, AnonymousUserException {
        UserDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/users/profile")).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(user.getFirstName());
        Assertions.assertThat(actualContent).contains(user.getAddress());
        Assertions.assertThat(actualContent).contains(user.getTelephone());
    }

    @Test
    void updateProfile_returnStatusOk() throws Exception {
        UserDTO user = getUserForProfile();
        when(userService.updateUserDetails(any())).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithoutParams_returnBadRequest() throws Exception, AnonymousUserException {
        UserDTO user = getFilledUser();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/users/profile")).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidFirstName_returnBadRequest() throws Exception, AnonymousUserException {
        UserDTO user = getFilledUser();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", "test$")
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidLastName_returnBadRequest() throws Exception, AnonymousUserException {
        UserDTO user = getFilledUser();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", "")
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidAddress_returnBadRequest() throws Exception, AnonymousUserException {
        UserDTO user = getFilledUser();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", "$$$address%%%")
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidTelephone_returnBadRequest() throws Exception, AnonymousUserException {
        UserDTO user = getFilledUser();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", "telephone")
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfile_callBusinessLogic() throws Exception {
        UserDTO user = getUserForProfile();
        when(userService.updateUserDetails(any())).thenReturn(user);
        mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
        verify(userService, times(1)).updateUserDetails(any());
    }

    @Test
    void updateProfile_returnUpdatedProfile() throws Exception {
        UserDTO returnedUser = getUserForProfile();
        when(userService.updateUserDetails(any())).thenReturn(returnedUser);
        MvcResult result = mockMvc.perform(post("/users/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("patronymic", VALID_PATRONYMIC)
                .param("email", VALID_EMAIL)
                .param("role", VALID_ROLE.name())
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_FIRST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_ADDRESS);
        Assertions.assertThat(actualContent).contains(VALID_TELEPHONE);
    }

    private UserDTO getUserForProfile() {
        UserDTO user = new UserDTO();
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setAddress(VALID_ADDRESS);
        user.setTelephone(VALID_TELEPHONE);
        return user;
    }

    private List<Integer> getPages(int countOfPages) {
        return IntStream.range(1, countOfPages + 1)
                .boxed()
                .collect(Collectors.toList());
    }

    private List<UserDTO> getOneUserList() {
        List<UserDTO> users = new ArrayList<>();
        UserDTO user = getFilledUser();
        users.add(user);
        return users;
    }

    private UserDTO getFilledUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(VALID_FIRST_NAME);
        userDTO.setLastName(VALID_LAST_NAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        userDTO.setEmail(VALID_EMAIL);
        userDTO.setRole(VALID_ROLE);
        userDTO.setUniqueNumber(VALID_UNIQUE_NUMBER);
        return userDTO;
    }

    private UserDTO getUserToAdd() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(VALID_FIRST_NAME);
        userDTO.setLastName(VALID_LAST_NAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        userDTO.setEmail(VALID_EMAIL);
        userDTO.setRole(VALID_ROLE);
        userDTO.setAddress(VALID_ADDRESS);
        userDTO.setTelephone(VALID_TELEPHONE);
        return userDTO;
    }

    private String generateStringByLength(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append('a');
        }
        return stringBuilder.toString();
    }

}