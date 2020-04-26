package com.gmail.yauhenizhukovich.app.service.util;

import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;

public class UserConversionUtil {

    public static LoginUserDTO convertDatabaseObjectToDTOToLogin(User user) {
        LoginUserDTO userDTO = new LoginUserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(getRole(user));
        return userDTO;
    }

    public static UsersDTO convertDatabaseObjectToUsersDTO(User user) {
        UsersDTO userDTO = new UsersDTO();
        userDTO.setUniqueNumber(user.getUniqueNumber());
        UserDetails userDetails = user.getUserDetails();
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setPatronymic(userDetails.getPatronymic());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(getRole(user));
        return userDTO;
    }

    public static UserDTO convertDatabaseObjectToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        UserDetails userDetails = user.getUserDetails();
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setPatronymic(userDetails.getPatronymic());
        userDTO.setUniqueNumber(user.getUniqueNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(getRole(user));
        return userDTO;
    }

    public static UserProfileDTO convertDatabaseObjectToUserProfileDTO(User user) {
        UserProfileDTO userDTO = new UserProfileDTO();
        UserDetails userDetails = user.getUserDetails();
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setAddress(userDetails.getAddress());
        userDTO.setTelephone(userDetails.getTelephone());
        return userDTO;
    }

    public static User convertDTOToDatabaseObject(AddUserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setRole(getRole(userDTO));
        UserDetails userDetails = getUserDetails(userDTO, user);
        user.setUserDetails(userDetails);
        return user;
    }

    private static UserDetails getUserDetails(AddUserDTO userDTO, User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setPatronymic(userDTO.getPatronymic());
        userDetails.setUser(user);
        return userDetails;
    }

    private static RoleEnumRepository getRole(AddUserDTO userDTO) {
        RoleEnumService serviceRole = userDTO.getRole();
        return RoleEnumRepository.valueOf(
                serviceRole.name());
    }

    private static RoleEnumService getRole(User user) {
        RoleEnumRepository repositoryRole = user.getRole();
        return RoleEnumService.valueOf(repositoryRole.name());
    }

}
