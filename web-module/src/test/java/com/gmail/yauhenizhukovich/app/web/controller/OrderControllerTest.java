package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.exception.CustomerDetailsException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.AddOrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.service.exception.CustomerDetailsException.CUSTOMER_DETAILS_EXCEPTION_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGES;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AMOUNT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_STATUS;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TELEPHONE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_UNIQUE_NUMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = {"SALE_USER", "CUSTOMER_USER"})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Test
    void getOrders_returnStatusOk() throws Exception {
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = getOrdersAndPages();
        when(orderService.getOrdersByPage(1)).thenReturn(ordersAndPages);
        mockMvc.perform(get("/orders")).andExpect(status().isOk());
    }

    @Test
    void getOrders_callBusinessLogic() throws Exception {
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = getOrdersAndPages();
        when(orderService.getOrdersByPage(eq(PAGE))).thenReturn(ordersAndPages);
        mockMvc.perform(get("/orders")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
        verify(orderService, times(1)).getOrdersByPage(eq(PAGE));
    }

    @Test
    void getOrders_returnOrders() throws Exception {
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = getOrdersAndPages();
        when(orderService.getOrdersByPage(eq(PAGE))).thenReturn(ordersAndPages);
        MvcResult result = mockMvc.perform(get("/orders")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        verify(orderService, times(1)).getOrdersByPage(eq(PAGE));
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains("New");
        Assertions.assertThat(actualContent).contains(String.valueOf(VALID_AMOUNT));
    }

    @Test
    void getOrder_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders/{id}", VALID_ID)).andExpect(status().isOk());
    }

    @Test
    void getOrder_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/orders/{id}", VALID_ID)).andExpect(status().isOk());
        verify(orderService, times(1)).getOrderById(eq(VALID_ID));
    }

    @Test
    void getOrder_returnOrder() throws Exception {
        OrderDTO order = getOrder();
        when(orderService.getOrderById(VALID_ID)).thenReturn(order);
        MvcResult result = mockMvc.perform(get("/orders/{id}", VALID_ID)).andReturn();
        verify(orderService, times(1)).getOrderById(eq(VALID_ID));
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_NAME);
        Assertions.assertThat(actualContent).contains(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualContent).contains(VALID_STATUS.name());
        Assertions.assertThat(actualContent).contains(String.valueOf(VALID_PRICE));
    }

    @Test
    void getOrderByNonexistentId_returnEmptyPage() throws Exception {
        when(orderService.getOrderById(VALID_ID)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/orders/{id}", VALID_ID)).andReturn();
        verify(orderService, times(1)).getOrderById(eq(VALID_ID));
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains("THIS ORDER OR CUSTOMER DOESNT EXIST");
    }

    @Test
    void addOrder_returnRedirectedUrl() throws Exception {
        mockMvc.perform(post("/orders")).andExpect(redirectedUrl("/profile/orders"));
    }

    @Test
    void addOrder_returnRedirectedUrlWithMessage() throws Exception, CustomerDetailsException {
        AddOrderDTO addOrder = new AddOrderDTO();
        addOrder.setAmount(VALID_AMOUNT);
        addOrder.setItemUniqueNumber(VALID_UNIQUE_NUMBER);
        when(orderService.addOrder(any())).thenThrow(new CustomerDetailsException());
        mockMvc.perform(post("/orders")
                .param("amount", String.valueOf(VALID_AMOUNT))
                .param("uniqueNumber", VALID_UNIQUE_NUMBER))
                .andExpect(redirectedUrl("/profile?error=" + CUSTOMER_DETAILS_EXCEPTION_MESSAGE));
    }

    private ObjectsDTOAndPagesEntity<OrdersDTO> getOrdersAndPages() {
        List<OrdersDTO> orders = new ArrayList<>();
        OrdersDTO order = new OrdersDTO();
        order.setStatus(VALID_STATUS);
        order.setAmount(VALID_AMOUNT);
        order.setPrice(VALID_PRICE);
        order.setItemName(VALID_NAME);
        orders.add(order);
        return new ObjectsDTOAndPagesEntity<>(PAGES, orders);
    }

    private OrderDTO getOrder() {
        OrderDTO order = new OrderDTO();
        order.setStatus(VALID_STATUS);
        order.setAmount(VALID_AMOUNT);
        order.setPrice(VALID_PRICE);
        order.setItemName(VALID_NAME);
        order.setCustomerTelephone(VALID_TELEPHONE);
        order.setCustomerUniqueNumber(VALID_UNIQUE_NUMBER);
        return order;
    }

}