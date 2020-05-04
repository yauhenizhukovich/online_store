package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.NONEXISTENT_ORDER_MESSAGE;

@RestController
@RequestMapping("/api/orders")
public class OrderAPIController {

    private final OrderService orderService;

    public OrderAPIController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping
    public List<OrdersDTO> getItems() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Object getOrder(
            @PathVariable Long id
    ) {
        OrderDTO order = orderService.getOrderById(id);
        if (order == null) {
            return NONEXISTENT_ORDER_MESSAGE;
        }
        return order;
    }

}
