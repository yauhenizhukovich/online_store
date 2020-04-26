package com.gmail.yauhenizhukovich.app.service.impl;

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
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import com.gmail.yauhenizhukovich.app.service.util.EmailUtil;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil;
import com.gmail.yauhenizhukovich.app.service.util.ValidationUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_USERS_BY_PAGE;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        ValidationUtil.validateEmail(email);
        User user = userRepository.getUserByEmail(email);
        return UserConversionUtil.convertDatabaseObjectToDTOToLogin(user);
    }

    @Override
    @Transactional
    public List<UserDTO> getUsersByPage(int page) {
        int startPosition = PaginationUtil.getStartPositionByPageNumber(page, COUNT_OF_USERS_BY_PAGE);
        List<User> users = userRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_USERS_BY_PAGE);
        return users.stream()
                .map(UserConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Integer> getPages() {
        Long countOfUsers = userRepository.getCountOfObjects();
        return PaginationUtil.getCountOfPages(countOfUsers, COUNT_OF_USERS_BY_PAGE);
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) throws UserExistenceException {
        ValidationUtil.validateUser(userDTO);
        if (userRepository.getUserByEmail(userDTO.getEmail()) != null) {
            throw new UserExistenceException("User with this email already exists.");
        }
        User user = UserConversionUtil.convertDTOToDatabaseObject(userDTO);
        setUUID(user);
        setPasswordAndSendToEmail(user);
        user = userRepository.add(user);
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getUserByUniqueNumber(String uniqueNumber) {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updatePasswordByUniqueNumber(String uniqueNumber) {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        setPasswordAndSendToEmail(user);
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateRoleByUniqueNumber(RoleEnumService serviceRole, String uniqueNumber) throws AdministratorChangingException {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        RoleEnumRepository actualRole = user.getRole();
        if (actualRole.equals(RoleEnumRepository.ADMINISTRATOR)) {
            int countOfAdministrators = getCountOfAdministrators();
            if (countOfAdministrators == 1) {
                throw new AdministratorChangingException("You should have at least one administrator!");
            }
        }
        RoleEnumRepository role = RoleEnumRepository.valueOf(serviceRole.name());
        if (!role.equals(actualRole)) {
            user.setRole(role);
        }
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException {
        int countOfDatabaseAdministrators = getCountOfDatabaseAdministrators();
        return deleteUsers(uniqueNumbers, countOfDatabaseAdministrators);
    }

    @Override
    @Transactional
    public UserDTO getUserProfile() throws AnonymousUserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email.equals("anonymousUser")) {
            throw new AnonymousUserException("You should log in to see your profile.");
        }
        User user = userRepository.getUserByEmail(email);
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUserDetails(UserDTO userDTO) {
        User user = userRepository.getUserByEmail(userDTO.getEmail());
        UserDetails userDetails = user.getUserDetails();
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setAddress(userDTO.getAddress());
        userDetails.setTelephone(userDTO.getTelephone());
        return UserConversionUtil.convertDatabaseObjectToDTO(user);
    }

    private void setUUID(User user) {
        UUID uniqueNumberUUID = UUID.randomUUID();
        String uniqueNumber = uniqueNumberUUID.toString();
        user.setUniqueNumber(uniqueNumber);
    }

    private void setPasswordAndSendToEmail(User user) {
        String password = RandomString.make();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        String email = user.getEmail();
        EmailUtil.sendPassword(email, password);
    }

    private int getCountOfDatabaseAdministrators() {
        Long countOfDatabaseAdministratorsLong = userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        return Math.toIntExact(countOfDatabaseAdministratorsLong);
    }

    private boolean deleteUsers(List<String> uniqueNumbers, int countOfDatabaseAdministrators) throws
            AdministratorChangingException {
        int countOfAdministratorsToDelete = 0;
        for (String uniqueNumber : uniqueNumbers) {
            User user = userRepository.getUserByUniqueNumber(uniqueNumber);
            RoleEnumRepository role = user.getRole();
            if (role.equals(RoleEnumRepository.ADMINISTRATOR)) {
                countOfAdministratorsToDelete++;
                if (countOfDatabaseAdministrators == countOfAdministratorsToDelete) {
                    throw new AdministratorChangingException("You can't delete all administrators!");
                }
            }
            userRepository.delete(user);
        }
        return true;
    }

    private int getCountOfAdministrators() {
        Long countOfAdministratorsLong = userRepository.getCountOfUsersByRole(RoleEnumRepository.ADMINISTRATOR);
        return Math.toIntExact(countOfAdministratorsLong);
    }

}
