package com.gmail.yauhenizhukovich.app.service.util;

import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;

public class UserConversionUtil {

    public static UserDTO convertDatabaseObjectToDTOToGetByEmail(User user) {
        UserDTO userDTO = new UserDTO();
        String email = user.getEmail();
        userDTO.setEmail(email);
        String password = user.getPassword();
        userDTO.setPassword(password);
        RoleEnum role = getRole(user);
        userDTO.setRole(role);
        return userDTO;
    }

    public static UserDTO convertAddedDatabaseObjectToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        String uniqueNumber = user.getUniqueNumber();
        userDTO.setUniqueNumber(uniqueNumber);
        String email = user.getEmail();
        userDTO.setEmail(email);
        String password = user.getPassword();
        userDTO.setPassword(password);
        RoleEnum role = getRole(user);
        userDTO.setRole(role);
        UserDetails userDetails = user.getUserDetails();
        String firstName = userDetails.getFirstName();
        userDTO.setFirstName(firstName);
        String lastName = userDetails.getLastName();
        userDTO.setLastName(lastName);
        String patronymic = userDetails.getPatronymic();
        userDTO.setPatronymic(patronymic);
        return userDTO;
    }

    public static User convertDTOToDatabaseObejctToAdd(UserDTO userDTO) {
        User user = new User();
        String email = userDTO.getEmail();
        user.setEmail(email);
        com.gmail.yauhenizhukovich.app.repository.model.RoleEnum role = getRole(userDTO);
        user.setRole(role);
        UserDetails userDetails = getUserDetails(userDTO, user);
        user.setUserDetails(userDetails);
        return user;
    }

    public static UserDTO convertDatabaseObjectToDTOToGetAll(User user) {
        UserDTO userDTO = new UserDTO();
        String uniqueNumber = user.getUniqueNumber();
        userDTO.setUniqueNumber(uniqueNumber);
        String email = user.getEmail();
        userDTO.setEmail(email);
        RoleEnum role = getRole(user);
        userDTO.setRole(role);
        UserDetails userDetails = user.getUserDetails();
        String firstName = userDetails.getFirstName();
        userDTO.setFirstName(firstName);
        String lastName = userDetails.getLastName();
        userDTO.setLastName(lastName);
        String patronymic = userDetails.getPatronymic();
        userDTO.setPatronymic(patronymic);
        return userDTO;
    }

    private static com.gmail.yauhenizhukovich.app.repository.model.RoleEnum getRole(UserDTO userDTO) {
        RoleEnum serviceRole = userDTO.getRole();
        return com.gmail.yauhenizhukovich.app.repository.model.RoleEnum.valueOf(
                serviceRole.name());
    }

    private static RoleEnum getRole(User user) {
        com.gmail.yauhenizhukovich.app.repository.model.RoleEnum repositoryRole = user.getRole();
        return RoleEnum.valueOf(repositoryRole.name());
    }

    private static UserDetails getUserDetails(UserDTO userDTO, User user) {
        UserDetails userDetails = new UserDetails();
        String lastName = userDTO.getLastName();
        userDetails.setLastName(lastName);
        String firstName = userDTO.getFirstName();
        userDetails.setFirstName(firstName);
        String patronymic = userDTO.getPatronymic();
        userDetails.setPatronymic(patronymic);
        userDetails.setUser(user);
        return userDetails;
    }

}
