package com.gmail.yauhenizhukovich.app.repository;

import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.model.Order;
import com.gmail.yauhenizhukovich.app.repository.model.User;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> getPaginatedOrdersByCustomer(int startPosition, int countOfOrdersByPage, User user);

    Long getCountOfOrdersByUser(User user);

}
