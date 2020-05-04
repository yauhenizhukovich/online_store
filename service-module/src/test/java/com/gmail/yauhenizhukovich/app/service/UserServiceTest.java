package com.gmail.yauhenizhukovich.app.service;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.impl.UserServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_USERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.START_POSITION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_EMAIL;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_PATRONYMIC;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ROLE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_UNIQUE_NUMBER;
import static com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException.ADMINISTRATOR_CHANGING_EXCEPTION_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.getUser;
import static com.gmail.yauhenizhukovich.app.service.util.ServiceUnitTestUtil.mockAuthentication;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil.convertDTOToDatabaseObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, emailService);
    }

    @Test
    public void getUserByValidEmail_returnValidUser() {
        User returnedUser = getUser();
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        LoginUserDTO actualUser = userService.getUserByEmail(VALID_EMAIL);
        Assertions.assertThat(actualUser).isNotNull();
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(returnedUser.getEmail());
        Assertions.assertThat(actualUser.getPassword()).isEqualTo(returnedUser.getPassword());
        RoleEnumService expectedRole = RoleEnumService.valueOf(returnedUser.getRole().name());
        Assertions.assertThat(actualUser.getRole()).isEqualTo(expectedRole);
    }

    @Test
    public void getUserByNonexistentEmail_returnNull() {
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(null);
        LoginUserDTO user = userService.getUserByEmail(VALID_EMAIL);
        Assertions.assertThat(user).isNull();
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
    }

    @Test
    public void getUsersByPage_returnUsers() {
        List<User> returnedUsers = getUsers();
        when(userRepository.getPaginatedObjects(START_POSITION, COUNT_OF_USERS_BY_PAGE))
                .thenReturn(returnedUsers);
        ObjectsDTOAndPagesEntity<UsersDTO> usersAndPages = userService.getUsersByPage(PAGE);
        List<UsersDTO> actualUsers = usersAndPages.getObjects();
        Assertions.assertThat(actualUsers).isNotNull();
        verify(userRepository, times(1)).getPaginatedObjects(START_POSITION, COUNT_OF_USERS_BY_PAGE);
        UsersDTO actualUser = actualUsers.get(0);
        User returnedUser = returnedUsers.get(0);
        Assertions.assertThat(actualUser.getUniqueNumber()).isEqualTo(returnedUser.getUniqueNumber());
        Assertions.assertThat(actualUser.getLastName()).isEqualTo(returnedUser.getUserDetails().getLastName());
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(returnedUser.getEmail());
    }

    @Test
    public void addValidUser_returnUser() throws UserExistenceException {
        AddUserDTO addedUser = getAddedUser();
        when(userRepository.getUserByEmail(addedUser.getEmail())).thenReturn(null);
        User returnedUser = getUser();
        when(userRepository.add(convertDTOToDatabaseObject(addedUser))).thenReturn(returnedUser);
        UserDTO actualUser = userService.addUser(addedUser);
        Assertions.assertThat(actualUser).isNotNull();
        verify(userRepository, times(1)).getUserByEmail(addedUser.getEmail());
        verify(userRepository, times(1)).add(convertDTOToDatabaseObject(addedUser));
        Assertions.assertThat(actualUser.getPatronymic()).isEqualTo(addedUser.getPatronymic());
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(addedUser.getEmail());
        Assertions.assertThat(actualUser.getRole()).isEqualTo(addedUser.getRole());
    }

    @Test
    public void getUserByValidUniqueNumber_returnValidUser() {
        User returnedUser = getUser();
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        UserDTO actualUser = userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualUser).isNotNull();
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualUser.getEmail()).isEqualTo(returnedUser.getEmail());
        Assertions.assertThat(actualUser.getPassword()).isEqualTo(returnedUser.getPassword());
        RoleEnumService expectedRole = RoleEnumService.valueOf(returnedUser.getRole().name());
        Assertions.assertThat(actualUser.getRole()).isEqualTo(expectedRole);
    }

    @Test
    public void getUserByNonexistentUniqueNumber_returnNull() {
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(null);
        UserDTO user = userService.getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(user).isNull();
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
    }

    @Test
    public void deleteUsersByUniqueNumberWithAdministratorLeft_returnActualStatus() throws AdministratorChangingException {
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.SALE_USER);
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(3L);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        boolean isDeleted = userService.deleteUsersByUniqueNumber(uniqueNumbers);
        verify(userRepository, times(1)).getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).delete(returnedUser);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteLastAdministrator_returnAdministratorChangingException() {
        mockAuthentication(VALID_EMAIL);
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(1L);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.ADMINISTRATOR);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        org.junit.jupiter.api.Assertions.assertThrows(
                AdministratorChangingException.class,
                () -> userService.deleteUsersByUniqueNumber(uniqueNumbers),
                ADMINISTRATOR_CHANGING_EXCEPTION_MESSAGE
        );
    }

    @Test
    public void updateUser_returnUpdatedUser() throws AdministratorChangingException {
        User returnedUser = getUser();
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setUniqueNumber(VALID_UNIQUE_NUMBER);
        updateUser.setRole(RoleEnumService.CUSTOMER_USER);
        UserDTO actualUser = userService.updateUser(updateUser);
        Assertions.assertThat(actualUser).isNotNull();
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualUser.getRole()).isEqualTo(updateUser.getRole());
    }

    @Test
    public void updateLastAdministrator_returnAdministratorChangingException() {
        mockAuthentication(VALID_EMAIL);
        User returnedUser = getUser();
        returnedUser.setRole(RoleEnumRepository.ADMINISTRATOR);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(1L);
        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setUniqueNumber(VALID_UNIQUE_NUMBER);
        updateUser.setRole(RoleEnumService.CUSTOMER_USER);
        org.junit.jupiter.api.Assertions.assertThrows(
                AdministratorChangingException.class,
                () -> userService.updateUser(updateUser),
                ADMINISTRATOR_CHANGING_EXCEPTION_MESSAGE
        );
    }

    private AddUserDTO getAddedUser() {
        AddUserDTO user = new AddUserDTO();
        user.setRole(VALID_ROLE);
        user.setEmail(VALID_EMAIL);
        user.setPatronymic(VALID_PATRONYMIC);
        return user;
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser());
        return users;
    }

}
