package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;

public interface UserService {

    LoginUserDTO getUserByEmail(String email);

    List<UsersDTO> getUsersByPage(int pageNumber);

    int getCountOfPages();

    UserDTO addUser(AddUserDTO user) throws UserExistenceException;

    UserDTO getUserByUniqueNumber(String uniqueNumber);

    boolean deleteUsersByUniqueNumber(List<String> uniqueNumbers) throws AdministratorChangingException;

    UserProfileDTO getUserProfile() throws AnonymousUserException;

    UserProfileDTO updateUserProfile(UpdateUserProfileDTO user);

    UserDTO updateUser(UpdateUserDTO updatedUser) throws AdministratorChangingException;

}
