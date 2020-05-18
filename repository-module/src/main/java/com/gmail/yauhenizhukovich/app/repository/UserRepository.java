package com.gmail.yauhenizhukovich.app.repository;

import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {

    User getUserByEmail(String email);

    User getUserByUniqueNumber(String uniqueNumber);

    Long getCountOfUsersByRole(RoleEnumRepository role);

}
