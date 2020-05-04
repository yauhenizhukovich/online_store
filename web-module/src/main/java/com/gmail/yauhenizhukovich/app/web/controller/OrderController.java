package com.gmail.yauhenizhukovich.app.web.controller;

import com.gmail.yauhenizhukovich.app.repository.model.StatusEnum;
import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.exception.CustomerDetailsException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.AddOrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.UpdateOrderDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.UPDATE_ORDER_MESSAGE;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping
    public String getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        ObjectsDTOAndPagesEntity<OrdersDTO> orders = orderService.getOrdersByPage(page);
        model.addAttribute("orders", orders.getObjects());
        model.addAttribute("pages", orders.getPages());
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(
            @PathVariable Long id,
            Model model
    ) {
        OrderDTO order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("statuses", StatusEnum.values());
        return "order";
    }

    @PostMapping
    public String addOrder(
            @ModelAttribute(name = "order") AddOrderDTO order
    ) {
        try {
            orderService.addOrder(order);
        } catch (CustomerDetailsException e) {
            return "redirect:/profile?error=" + e.getMessage();
        }
        return "redirect:/profile/orders";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute(name = "order") UpdateOrderDTO updateOrder,
            Model model
    ) {
        updateOrder.setId(id);
        OrderDTO order = orderService.updateOrder(updateOrder);
        model.addAttribute("order", order);
        model.addAttribute("statuses", StatusEnum.values());
        model.addAttribute("message", UPDATE_ORDER_MESSAGE);
        return "order";
    }

}
