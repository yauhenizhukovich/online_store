package com.gmail.yauhenizhukovich.app.service.util;

import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;

public class UserConversionUtil {

    public static UserDTO convertDatabaseObjectToDTOToLogin(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(getRole(user));
        return userDTO;
    }

    public static UserDTO convertDatabaseObjectToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUniqueNumber(user.getUniqueNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(getRole(user));
        UserDetails userDetails = user.getUserDetails();
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setPatronymic(userDetails.getPatronymic());
        userDTO.setAddress(userDetails.getAddress());
        userDTO.setTelephone(userDetails.getTelephone());
        return userDTO;
    }

    public static User convertDTOToDatabaseObject(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setRole(getRole(userDTO));
        UserDetails userDetails = getUserDetails(userDTO, user);
        user.setUserDetails(userDetails);
        return user;
    }

    private static RoleEnumRepository getRole(UserDTO userDTO) {
        RoleEnumService serviceRole = userDTO.getRole();
        return RoleEnumRepository.valueOf(
                serviceRole.name());
    }

    private static RoleEnumService getRole(User user) {
        RoleEnumRepository repositoryRole = user.getRole();
        return RoleEnumService.valueOf(repositoryRole.name());
    }

    private static UserDetails getUserDetails(UserDTO userDTO, User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setPatronymic(userDTO.getPatronymic());
        userDetails.setAddress(userDTO.getAddress());
        userDetails.setTelephone(userDTO.getTelephone());
        userDetails.setUser(user);
        return userDetails;
    }

}
