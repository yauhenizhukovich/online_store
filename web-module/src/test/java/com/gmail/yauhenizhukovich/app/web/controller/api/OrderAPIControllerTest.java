package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.OrderService;
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

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.NONEXISTENT_ORDER_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AMOUNT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_STATUS;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TELEPHONE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_UNIQUE_NUMBER;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderAPIController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
class OrderAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    @Test
    void getOrders_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
    }

    @Test
    void getOrders_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrders_returnOrders() throws Exception {
        List<OrdersDTO> orders = getOrders();
        when(orderService.getAllOrders()).thenReturn(orders);
        MvcResult result = mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(orders);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void getOrderById_returnStatusOk() throws Exception {
        OrderDTO order = getOrder();
        when(orderService.getOrderById(VALID_ID)).thenReturn(order);
        mockMvc.perform(get("/api/orders/{id}", VALID_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderByNonexistentId_returnErrorMessage() throws Exception {
        when(orderService.getOrderById(VALID_ID)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/api/orders/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).isEqualTo(NONEXISTENT_ORDER_MESSAGE);
    }

    @Test
    void getOrderById_callBusinessLogic() throws Exception {
        OrderDTO order = getOrder();
        when(orderService.getOrderById(VALID_ID)).thenReturn(order);
        mockMvc.perform(get("/api/orders/{id}", VALID_ID))
                .andExpect(status().isOk());
        verify(orderService, times(1)).getOrderById(VALID_ID);
    }

    @Test
    void getOrderById_returnValidOrder() throws Exception {
        OrderDTO order = getOrder();
        when(orderService.getOrderById(VALID_ID)).thenReturn(order);
        MvcResult result = mockMvc.perform(get("/api/orders/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(order);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    private List<OrdersDTO> getOrders() {
        List<OrdersDTO> orders = new ArrayList<>();
        OrdersDTO order = new OrdersDTO();
        order.setItemName(VALID_NAME);
        order.setAmount(VALID_AMOUNT);
        order.setPrice(VALID_PRICE);
        order.setStatus(VALID_STATUS);
        orders.add(order);
        return orders;
    }

    private OrderDTO getOrder() {
        OrderDTO order = new OrderDTO();
        order.setPrice(VALID_PRICE);
        order.setStatus(VALID_STATUS);
        order.setAmount(VALID_AMOUNT);
        order.setItemName(VALID_NAME);
        order.setCustomerUniqueNumber(VALID_UNIQUE_NUMBER);
        order.setCustomerTelephone(VALID_TELEPHONE);
        return order;
    }

}