package com.gmail.yauhenizhukovich.app.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import com.gmail.yauhenizhukovich.app.service.util.EmailUtil;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import com.gmail.yauhenizhukovich.app.service.util.RandomUtil;
import com.gmail.yauhenizhukovich.app.service.util.UserConversionUtil;
import com.gmail.yauhenizhukovich.app.service.util.ValidationUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        ValidationUtil.validateEmail(email);
        User user = userRepository.getUserByEmail(email);
        return UserConversionUtil.convertDatabaseObjectToDTOToGetByEmail(user);
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        ValidationUtil.validateUserDTO(userDTO);
        User user = UserConversionUtil.convertDTOToDatabaseObejctToAdd(userDTO);
        setUUID(user);
        setPasswordAndSendToEmail(user);
        user = userRepository.add(user);
        return UserConversionUtil.convertAddedDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public List<UserDTO> getUsersByPage(int pageNumber) {
        int usersByPage = PaginationUtil.getCountOfUsersByPage();
        int startPosition = PaginationUtil.getStartPositionByPageNumber(pageNumber, usersByPage);
        List<User> users = userRepository.getObjectsByStartPositionAndMaxResult(startPosition, usersByPage);
        return users.stream()
                .map(UserConversionUtil::convertDatabaseObjectToDTOToGetAll)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException {
        int countOfDatabaseAdministrators = getCountOfDatabaseAdministrators();
        return deleteUsers(uniqueNumbers, countOfDatabaseAdministrators);
    }

    @Override
    @Transactional
    public List<Integer> getListOfPageNumbers() {
        Long countOfUsers = userRepository.getCountOfObjects();
        int usersByPage = PaginationUtil.getCountOfUsersByPage();
        return PaginationUtil.getCountOfPages(countOfUsers, usersByPage);
    }

    @Override
    @Transactional
    public String updatePasswordByUniqueNumber(String uniqueNumber) {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        return setPasswordAndSendToEmail(user);
    }

    @Override
    @Transactional
    public boolean updateRoleByUniqueNumber(com.gmail.yauhenizhukovich.app.service.model.RoleEnum serviceRole, String uniqueNumber) throws AdministratorChangingException {
        User user = userRepository.getUserByUniqueNumber(uniqueNumber);
        RoleEnum actualRole = user.getRole();
        if (actualRole.equals(RoleEnum.ADMINISTRATOR)) {
            int countOfAdministrators = getCountOfAdministrators();
            if (countOfAdministrators == 1) {
                throw new AdministratorChangingException("You should have at least one administrator!");
            }
        }
        RoleEnum role = RoleEnum.valueOf(serviceRole.name());
        if (!role.equals(actualRole)) {
            user.setRole(role);
            return true;
        }
        return false;
    }

    private void setUUID(User user) {
        UUID uniqueNumberUUID = UUID.randomUUID();
        String uniqueNumber = uniqueNumberUUID.toString();
        user.setUniqueNumber(uniqueNumber);
    }

    private String setPasswordAndSendToEmail(User user) {
        String password = RandomUtil.generateUserPassword();
        String email = user.getEmail();
        EmailUtil.sendPassword(email, password);
        String bCryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(bCryptPassword);
        return bCryptPassword;
    }

    private int getCountOfDatabaseAdministrators() {
        Long countOfDatabaseAdministratorsLong = userRepository.getCountOfUsersByRole(com.gmail.yauhenizhukovich.app.repository.model.RoleEnum.ADMINISTRATOR);
        return Math.toIntExact(countOfDatabaseAdministratorsLong);
    }

    private boolean deleteUsers(List<String> uniqueNumbers, int countOfDatabaseAdministrators) throws
            AdministratorChangingException {
        int countOfAdministratorsToDelete = 0;
        for (String uniqueNumber : uniqueNumbers) {
            User user = userRepository.getUserByUniqueNumber(uniqueNumber);
            RoleEnum role = user.getRole();
            if (role.equals(RoleEnum.ADMINISTRATOR)) {
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
        Long countOfAdministratorsLong = userRepository.getCountOfUsersByRole(RoleEnum.ADMINISTRATOR);
        return Math.toIntExact(countOfAdministratorsLong);
    }

}
