package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;

public interface UserService {

    UserDTO getUserByEmail(String email);

    UserDTO addUser(UserDTO user);

    List<UserDTO> getUsersByPage(int pageNumber);

    boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException;

    List<Integer> getListOfPageNumbers();

    String updatePasswordByUniqueNumber(String uniqueNumber);

    boolean updateRoleByUniqueNumber(RoleEnum role, String uniqueNumber) throws AdministratorChangingException;

}
