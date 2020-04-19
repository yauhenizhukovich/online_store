package com.gmail.yauhenizhukovich.app.repository;

import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {

    User getUserByEmail(String email);

    User getUserByUniqueNumber(String uniqueNumber);

    Long getCountOfUsersByRole(RoleEnum role);

}
