package com.gmail.yauhenizhukovich.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;
import com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_USERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil.convertDatabaseObjectToDTOToLogin;
import static com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil.convertDatabaseObjectToUserDTO;
import static com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil.convertDatabaseObjectToUserProfileDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public List<UsersDTO> getUsersByPage(int page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_USERS_BY_PAGE);
        List<User> users = userRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_USERS_BY_PAGE);
        return users.stream()
                .map(UserConversionUtil::convertDatabaseObjectToUsersDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountOfPages() {
        Long countOfUsers = userRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfUsers, COUNT_OF_USERS_BY_PAGE);
    }

    @Override
    public UserDTO addUser(AddUserDTO userDTO) throws UserExistenceException {
        String email = userDTO.getEmail();
        if (userRepository.getUserByEmail(email) != null) {
            throw new UserExistenceException("UserExistenceException");     // TODO
        }
        User user = convertDTOToDatabaseObject(userDTO);
        UUID uniqueNumber = UUID.randomUUID();
        user.setUniqueNumber(uniqueNumber.toString());
        setPasswordAndSendToEmail(user);
        user = userRepository.add(user);
        return convertDatabaseObjectToUserDTO(user);
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
                    throw new AdministratorChangingException("exc");
                }
            }
            usersToDelete.add(user);
        }
        usersToDelete.forEach(userRepository::delete);
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
                    throw new AdministratorChangingException("AdministratorChangingException");    //  TODO
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) {
            throw new AnonymousUserException("AnonymousUserException");    // TODO
        }
        User user = userRepository.getUserByEmail(authentication.getName());
        return convertDatabaseObjectToUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO updateUserProfile(UpdateUserProfileDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByEmail(authentication.getName());
        UserDetails userDetails = user.getUserDetails();
        setUserDetails(userDTO, userDetails);
        if (userDTO.getUpdatePassword()) {
            setPasswordAndSendToEmail(user);
        }
        return convertDatabaseObjectToUserProfileDTO(user);
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
        //        EmailUtil.sendPassword(user.getEmail(), password);            TODO
    }

    private int getCountOfDatabaseAdministrators() {
        Long countOfDatabaseAdministratorsLong = userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        return Math.toIntExact(countOfDatabaseAdministratorsLong);
    }

}
