package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;

public interface UserService {

    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsersByPage(int pageNumber);

    List<Integer> getPages();

    UserDTO addUser(UserDTO user) throws UserExistenceException;

    UserDTO getUserByUniqueNumber(String uniqueNumber);

    UserDTO updatePasswordByUniqueNumber(String uniqueNumber);

    UserDTO updateRoleByUniqueNumber(RoleEnumService role, String uniqueNumber) throws AdministratorChangingException;

    boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException;

    UserDTO getUserProfile() throws AnonymousUserException;

    UserDTO updateUserDetails(UserDTO user);

}
