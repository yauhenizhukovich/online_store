package com.gmail.yauhenizhukovich.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.impl.UserServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_USERS_BY_PAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static final String VALID_FIRSTNAME = "test";
    public static final String VALID_LASTNAME = "test";
    public static final String VALID_PATRONYMIC = "test";
    public static final String VALID_EMAIL = "yauhenizhukovich@gmail.com";
    public static final String VALID_PASSWORD = "test";
    public static final String VALID_UNIQUE_NUMBER = "test";
    public static final RoleEnumService VALID_ROLE = RoleEnumService.SECURE_API_USER;
    public static final String INVALID_EMAIL = "test";
    public static final String INVALID_FIRSTNAME = "test1";
    public static final String INVALID_LASTNAME = "test%";
    private static final String INVALID_PATRONYMIC = "test ";
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void getUserByValidEmail_returnUserDTO() {
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.CUSTOMER_USER);
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        UserDTO returnedUserDTO = UserConversionUtil.convertDatabaseObjectToDTOToLogin(returnedUser);
        UserDTO actualUserDTO = userService.getUserByEmail(VALID_EMAIL);
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
        Assertions.assertThat(actualUserDTO).isNotNull();
        Assertions.assertThat(actualUserDTO).isEqualTo(returnedUserDTO);
    }

    @Test
    public void getUserByInvalidEmail_returnNotValidException() {
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserByEmail(INVALID_EMAIL),
                "Invalid email."
        );
    }

    @Test
    public void addUserWithInvalidEmail_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(INVALID_EMAIL);
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid email."
        );
    }

    @Test
    public void addUserWithInvalidSmallFirstName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidFirstName = generateStringByLength(1);
        userDTO.setFirstName(invalidFirstName);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid first name."
        );
    }

    @Test
    public void addUserWithInvalidBigFirstName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidFirstName = generateStringByLength(21);
        userDTO.setFirstName(invalidFirstName);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid first name."
        );
    }

    @Test
    public void addUserWithInvalidSmallLastName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidLastName = generateStringByLength(1);
        userDTO.setLastName(invalidLastName);
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid last name."
        );
    }

    @Test
    public void addUserWithInvalidBigLastName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidLastName = generateStringByLength(41);
        userDTO.setLastName(invalidLastName);
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid last name."
        );
    }

    @Test
    public void addUserWithInvalidBigPatronymic_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidPatronymic = generateStringByLength(44);
        userDTO.setPatronymic(invalidPatronymic);
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setLastName(VALID_LASTNAME);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid patronymic."
        );
    }

    @Test
    public void addUserWithInvalidPatternFirstName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(INVALID_FIRSTNAME);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid first name."
        );
    }

    @Test
    public void addUserWithInvalidPatternLastName_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setLastName(INVALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid last name."
        );
    }

    @Test
    public void addUserWithInvalidPatternPatronymic_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(INVALID_PATRONYMIC);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDTO),
                "Invalid patronymic."
        );
    }

    @Test
    public void getUsersByPage_returnUsers() {
        int pageNumber = 5;
        int startPosition = PaginationUtil.getStartPositionByPageNumber(pageNumber, COUNT_OF_USERS_BY_PAGE);
        List<User> returnedUsers = new ArrayList<>();
        when(userRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_USERS_BY_PAGE))
                .thenReturn(returnedUsers);
        List<UserDTO> returnedUsersDTO = returnedUsers.stream()
                .map(UserConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
        List<UserDTO> actualUsersDTO = userService.getUsersByPage(pageNumber);
        verify(userRepository, times(1)).getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_USERS_BY_PAGE);
        Assertions.assertThat(actualUsersDTO).isNotNull();
        Assertions.assertThat(actualUsersDTO).isEqualTo(returnedUsersDTO);
    }

    @Test
    public void deleteUsersByUniqueNumberWithAdministratorLeft_returnActualStatus() throws AdministratorChangingException {
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.SALE_USER);
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(3L);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        userService.deleteUsersByUniqueNumber(uniqueNumbers);
        verify(userRepository, times(1)).getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).delete(returnedUser);
    }

    @Test
    public void deleteLastAdministrator_returnAdministratorChangingException() {
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(1L);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.ADMINISTRATOR);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        org.junit.jupiter.api.Assertions.assertThrows(
                AdministratorChangingException.class,
                () -> userService.deleteUsersByUniqueNumber(uniqueNumbers),
                "You cant delete all administrators."
        );
    }

    @Test
    public void getListOfPageNumbers_returnListOfPages() {
        Long countOfObjects = 30L;
        when(userRepository.getCountOfObjects()).thenReturn(countOfObjects);
        List<Integer> returnedCountOfPages = getReturnedCountOfPages(countOfObjects);
        List<Integer> actualCountOfPages = userService.getPages();
        verify(userRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(actualCountOfPages).isNotNull();
        Assertions.assertThat(actualCountOfPages).isEqualTo(returnedCountOfPages);
    }

    @Test
    public void updateRoleByUniqueNumber_returnActualStatus() throws AdministratorChangingException {
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.ADMINISTRATOR);
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(VALID_FIRSTNAME);
        userDetails.setLastName(VALID_LASTNAME);
        returnedUser.setUserDetails(userDetails);
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(3L);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        UserDTO actualResult = userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        Assertions.assertThat(actualResult).isNotNull();
    }

    @Test
    public void updateRoleByUniqueNumber_returnIsUpdated() {
        when(userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR)).thenReturn(1L);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnumRepository.ADMINISTRATOR);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        org.junit.jupiter.api.Assertions.assertThrows(
                AdministratorChangingException.class,
                () -> userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER),
                "You cant have no administrators."
        );
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(RoleEnumService.CUSTOMER_USER);
        userDTO.setEmail(VALID_EMAIL);
        userDTO.setFirstName(VALID_FIRSTNAME);
        userDTO.setLastName(VALID_LASTNAME);
        userDTO.setPatronymic(VALID_PATRONYMIC);
        return userDTO;
    }

    private User getUser() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRole(RoleEnumRepository.SALE_USER);
        UserDetails userDetails = new UserDetails();
        user.setUserDetails(userDetails);
        return user;
    }

    private List<Integer> getReturnedCountOfPages(Long countOfObjects) {
        return PaginationUtil.getCountOfPages(countOfObjects, COUNT_OF_USERS_BY_PAGE);
    }

    private String generateStringByLength(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append('a');
        }
        return stringBuilder.toString();
    }

}
