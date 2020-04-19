package com.gmail.yauhenizhukovich.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.impl.UserServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.RandomUtil;
import com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
    public static final com.gmail.yauhenizhukovich.app.service.model.RoleEnum VALID_ROLE = com.gmail.yauhenizhukovich.app.service.model.RoleEnum.SECURE_API_USER;
    public static final String INVALID_EMAIL = "test";
    public static final String INVALID_FIRSTNAME = "test1";
    public static final String INVALID_LASTNAME = "test%";
    private static final String INVALID_PATRONYMIC = "test ";
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void getUserByValidEmail_returnUserDTO() {
        User returnedUser = new User();
        returnedUser.setRole(RoleEnum.CUSTOMER_USER);
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        UserDTO returnedUserDTO = UserConversionUtil.convertDatabaseObjectToDTOToGetByEmail(returnedUser);
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
    public void addValidUser_returnUserDTO() {
        UserDTO userDTO = getUserDTO();
        User returnedUser = getUser();
        when(userRepository.add(any())).thenReturn(returnedUser);
        UserDTO actualUserDTO = userService.addUser(userDTO);
        UserDTO returnedUserDTO = UserConversionUtil.convertAddedDatabaseObjectToDTO(returnedUser);
        verify(userRepository, times(1)).add(any());
        Assertions.assertThat(actualUserDTO).isNotNull();
        Assertions.assertThat(actualUserDTO).isEqualTo(returnedUserDTO);
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
    public void addUserWithInvalidSmallPatronymic_returnNotValidException() {
        UserDTO userDTO = new UserDTO();
        String invalidPatronymic = generateStringByLength(1);
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
        int countOfUsersByPage = PaginationUtil.getCountOfUsersByPage();
        int startPosition = PaginationUtil.getStartPositionByPageNumber(pageNumber, countOfUsersByPage);
        List<User> returnedUsers = new ArrayList<>();
        when(userRepository.getObjectsByStartPositionAndMaxResult(startPosition, countOfUsersByPage))
                .thenReturn(returnedUsers);
        List<UserDTO> returnedUsersDTO = returnedUsers.stream()
                .map(UserConversionUtil::convertDatabaseObjectToDTOToGetAll)
                .collect(Collectors.toList());
        List<UserDTO> actualUsersDTO = userService.getUsersByPage(pageNumber);
        verify(userRepository, times(1)).getObjectsByStartPositionAndMaxResult(startPosition, countOfUsersByPage);
        Assertions.assertThat(actualUsersDTO).isNotNull();
        Assertions.assertThat(actualUsersDTO).isEqualTo(returnedUsersDTO);
    }

    @Test
    public void deleteUsersByUniqueNumberWithAdministratorLeft_returnActualStatus() throws AdministratorChangingException {
        List<String> uniqueNumbers = new ArrayList<>();
        uniqueNumbers.add(VALID_UNIQUE_NUMBER);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnum.SALE_USER);
        when(userRepository.getCountOfUsersByRole(RoleEnum.ADMINISTRATOR)).thenReturn(3L);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        when(userRepository.delete(returnedUser)).thenReturn(true);
        boolean actualStatus = userService.deleteUsersByUniqueNumber(uniqueNumbers);
        verify(userRepository, times(1)).getCountOfUsersByRole(RoleEnum.ADMINISTRATOR);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).delete(returnedUser);
        Assertions.assertThat(actualStatus).isNotNull();
        Assertions.assertThat(actualStatus).isEqualTo(true);
    }

    @Test
    public void deleteLastAdministrator_returnAdministratorChangingException() {
        when(userRepository.getCountOfUsersByRole(RoleEnum.ADMINISTRATOR)).thenReturn(1L);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnum.ADMINISTRATOR);
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
        List<Integer> actualCountOfPages = userService.getListOfPageNumbers();
        verify(userRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(actualCountOfPages).isNotNull();
        Assertions.assertThat(actualCountOfPages).isEqualTo(returnedCountOfPages);
    }

    @Test
    public void updatePasswordByUniqueNumber_returnNotNullPassword() {
        User returnedUser = new User();
        returnedUser.setEmail(VALID_EMAIL);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        String actualBCryptPassword = userService.updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualBCryptPassword).isNotNull();
    }

    @Test
    public void updatePasswordByUniqueNumber_returnNotEqualToPreviousPassword() {
        User returnedUser = getUser();
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        String actualBCryptPassword = userService.updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        boolean isUpdatedPasswordEqualsToPrevious = BCrypt.checkpw(VALID_PASSWORD, actualBCryptPassword);
        Assertions.assertThat(isUpdatedPasswordEqualsToPrevious).isEqualTo(false);
    }

    @Test
    public void updatePasswordByUniqueNumber_returnUpdatedBCryptPassword() {
        User returnedUser = new User();
        returnedUser.setEmail(VALID_EMAIL);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        String actualBCryptPassword = userService.updatePasswordByUniqueNumber(VALID_UNIQUE_NUMBER);
        String password = RandomUtil.generateUserPassword();
        String returnedBCryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Assertions.assertThat(actualBCryptPassword.length()).isEqualTo(returnedBCryptPassword.length());
    }

    @Test
    public void updateRoleByUniqueNumber_returnActualStatus() throws AdministratorChangingException {
        User returnedUser = new User();
        returnedUser.setRole(RoleEnum.ADMINISTRATOR);
        when(userRepository.getCountOfUsersByRole(RoleEnum.ADMINISTRATOR)).thenReturn(3L);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        boolean actualStatus = userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getUserByUniqueNumber(VALID_UNIQUE_NUMBER);
        verify(userRepository, times(1)).getCountOfUsersByRole(RoleEnum.ADMINISTRATOR);
        Assertions.assertThat(actualStatus).isNotNull();
        Assertions.assertThat(actualStatus).isEqualTo(true);
    }

    @Test
    public void updateRoleByUniqueNumber_returnIsUpdated() {
        when(userRepository.getCountOfUsersByRole(RoleEnum.ADMINISTRATOR)).thenReturn(1L);
        User returnedUser = new User();
        returnedUser.setRole(RoleEnum.ADMINISTRATOR);
        when(userRepository.getUserByUniqueNumber(VALID_UNIQUE_NUMBER)).thenReturn(returnedUser);
        org.junit.jupiter.api.Assertions.assertThrows(
                AdministratorChangingException.class,
                () -> userService.updateRoleByUniqueNumber(VALID_ROLE, VALID_UNIQUE_NUMBER),
                "You cant have no administrators."
        );
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(com.gmail.yauhenizhukovich.app.service.model.RoleEnum.CUSTOMER_USER);
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
        user.setRole(RoleEnum.SALE_USER);
        UserDetails userDetails = new UserDetails();
        user.setUserDetails(userDetails);
        return user;
    }

    private List<Integer> getReturnedCountOfPages(Long countOfObjects) {
        int usersByPage = PaginationUtil.getCountOfReviewsByPage();
        return PaginationUtil.getCountOfPages(countOfObjects, usersByPage);
    }

    private String generateStringByLength(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append('a');
        }
        return stringBuilder.toString();
    }

}
