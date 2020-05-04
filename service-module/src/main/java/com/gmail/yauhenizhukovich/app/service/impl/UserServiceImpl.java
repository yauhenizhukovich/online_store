package com.gmail.yauhenizhukovich.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.EmailService;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ANONYMOUS_USER_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.MailConstant.MAIL_TEXT;
import static com.gmail.yauhenizhukovich.app.service.constant.MailConstant.MAIL_TITLE;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_USERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil.convertDatabaseObjectToDTOToLogin;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil.convertDatabaseObjectToUserDTO;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.UserConversionUtil.convertDatabaseObjectToUserProfileDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserServiceImpl(
            UserRepository userRepository,
            @Lazy PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDTO addUser(AddUserDTO userDTO) throws UserExistenceException {
        String email = userDTO.getEmail();
        if (userRepository.getUserByEmail(email) != null) {
            throw new UserExistenceException();
        }
        User user = convertDTOToDatabaseObject(userDTO);
        UUID uniqueNumber = UUID.randomUUID();
        user.setUniqueNumber(uniqueNumber.toString());
        setPasswordAndSendToEmail(user);
        user = userRepository.add(user);
        return convertDatabaseObjectToUserDTO(user);
    }

    @Override
    public ObjectsDTOAndPagesEntity<UsersDTO> getUsersByPage(int page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_USERS_BY_PAGE);
        List<User> users = userRepository.getPaginatedObjects(startPosition, COUNT_OF_USERS_BY_PAGE);
        int pages = getPages();
        List<UsersDTO> usersDTO = getUsersDTO(users);
        return new ObjectsDTOAndPagesEntity<>(pages, usersDTO);
    }

    @Override
    public LoginUserDTO getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        return convertDatabaseObjectToDTOToLogin(user);
    }

    @Override
    public UserDTO getUserByUniqueNumber(String uniqueNumber) {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        if (user == null) {
            return null;
        }
        return convertDatabaseObjectToUserDTO(user);
    }

    @Override
    public boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException {
        int countOfDatabaseAdministrators = getCountOfDatabaseAdministrators();
        int countOfAdministratorsToDelete = 0;
        List<User> usersToDelete = new ArrayList<>();
        for (String uniqueNumber : uniqueNumbers) {
            User user = userRepository.getUserByUniqueNumber(uniqueNumber);
            if (user.getRole() == RoleEnumRepository.ADMINISTRATOR) {
                countOfAdministratorsToDelete++;
                if (countOfDatabaseAdministrators == countOfAdministratorsToDelete) {
                    logger.error(getAuthenticationName() + " tried to delete last administrators.");
                    throw new AdministratorChangingException();
                }
            }
            usersToDelete.add(user);
        }
        for (User user : usersToDelete) {
            user.getArticles().forEach(article -> article.setAuthor(null));
            user.getOrders().forEach(order -> order.setCustomer(null));
            userRepository.delete(user);
        }
        return true;
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO updatedUser) throws AdministratorChangingException {
        User user = userRepository.getUserByUniqueNumber(updatedUser.getUniqueNumber());
        if (updatedUser.isUpdatePassword()) {
            setPasswordAndSendToEmail(user);
        }
        if (user.getRole() == RoleEnumRepository.ADMINISTRATOR) {
            if (updatedUser.getRole() != RoleEnumService.ADMINISTRATOR) {
                int countOfAdministrators = getCountOfDatabaseAdministrators();
                if (countOfAdministrators == 1) {
                    logger.error(getAuthenticationName() + " tried to change last administrators role.");
                    throw new AdministratorChangingException();
                }
            }
        }
        RoleEnumRepository updatedRole = RoleEnumRepository.valueOf(updatedUser.getRole().name());
        if (updatedRole != user.getRole()) {
            user.setRole(updatedRole);
        }
        return convertDatabaseObjectToUserDTO(user);
    }

    @Override
    public UserProfileDTO getUserProfile() throws AnonymousUserException {
        String authenticationName = getAuthenticationName();
        if (authenticationName.equals(ANONYMOUS_USER_NAME)) {
            throw new AnonymousUserException();
        }
        User user = userRepository.getUserByEmail(authenticationName);
        return convertDatabaseObjectToUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO updateUserProfile(UpdateUserProfileDTO userDTO) {
        String email = getAuthenticationName();
        User user = userRepository.getUserByEmail(email);
        UserDetails userDetails = user.getUserDetails();
        setUserDetails(userDTO, userDetails);
        if (userDTO.getUpdatePassword()) {
            setPasswordAndSendToEmail(user);
        }
        return convertDatabaseObjectToUserProfileDTO(user);
    }

    private String getAuthenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private int getPages() {
        Long countOfReviews = userRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfReviews, COUNT_OF_USERS_BY_PAGE);
    }

    private List<UsersDTO> getUsersDTO(List<User> users) {
        return users.stream()
                .map(UserConversionUtil::convertDatabaseObjectToUsersDTO)
                .collect(Collectors.toList());
    }

    private void setUserDetails(UpdateUserProfileDTO userDTO, UserDetails userDetails) {
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setAddress(userDTO.getAddress());
        userDetails.setTelephone(userDTO.getTelephone());
    }

    private void setPasswordAndSendToEmail(User user) {
        String password = RandomString.make();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        logger.debug("User password was successfully updated.");
        emailService.sendMessage(user.getEmail(), MAIL_TITLE, MAIL_TEXT + password);
        logger.debug("Email was successfully sent.");
    }

    private int getCountOfDatabaseAdministrators() {
        Long countOfDatabaseAdministratorsLong = userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        return Math.toIntExact(countOfDatabaseAdministratorsLong);
    }

}
