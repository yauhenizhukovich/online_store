package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.CustomerDetailsException;
import com.gmail.yauhenizhukovich.app.service.model.order.AddOrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.UpdateOrderDTO;

public interface OrderService {

    ObjectsDTOAndPagesEntity<OrdersDTO> getOrdersByPage(Integer page);

    OrderDTO getOrderById(Long id);

    List<OrdersDTO> getAllOrders();

    OrderDTO updateOrder(UpdateOrderDTO updateOrder);

    OrderDTO addOrder(AddOrderDTO order) throws CustomerDetailsException;

    ObjectsDTOAndPagesEntity<OrdersDTO> getUserOrdersByPage(Integer page) throws AnonymousUserException;

}
