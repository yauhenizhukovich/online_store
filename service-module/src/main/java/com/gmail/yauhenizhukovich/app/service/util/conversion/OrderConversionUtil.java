package com.gmail.yauhenizhukovich.app.service.util.conversion;

import com.gmail.yauhenizhukovich.app.repository.model.Order;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.model.order.AddOrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;

public class OrderConversionUtil {

    public static OrdersDTO convertDatabaseObjectToOrdersDTO(Order order) {
        OrdersDTO orderDTO = new OrdersDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItemName(order.getItem().getName());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setPrice(order.getPrice());
        return orderDTO;
    }

    public static OrderDTO convertDatabaseObjectToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItemName(order.getItem().getName());
        User customer = order.getCustomer();
        if (customer != null) {
            orderDTO.setCustomerUniqueNumber(customer.getUniqueNumber());
            orderDTO.setCustomerTelephone(customer.getUserDetails().getTelephone());
        }
        orderDTO.setAmount(order.getAmount());
        orderDTO.setPrice(order.getPrice());
        return orderDTO;
    }

    public static Order convertDTOToDatabaseObject(AddOrderDTO orderDTO) {
        Order order = new Order();
        order.setAmount(orderDTO.getAmount());
        return order;
    }

}