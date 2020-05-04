package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGES;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ADDRESS;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_AMOUNT;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_STATUS;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_TELEPHONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    @Test
    void getProfile_returnStatusOk() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(get("/profile")).andExpect(status().isOk());
    }

    @Test
    void getProfile_callBusinessLogic() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(get("/profile")).andExpect(status().isOk());
        verify(userService, times(1)).getUserProfile();
    }

    @Test
    void getProfile_returnUserForProfile() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/profile")).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(user.getFirstName());
        Assertions.assertThat(actualContent).contains(user.getAddress());
        Assertions.assertThat(actualContent).contains(user.getTelephone());
    }

    @Test
    void updateProfile_returnStatusOk() throws Exception {
        UserProfileDTO user = getUserForProfile();
        when(userService.updateUserProfile(any())).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithoutParams_returnBadRequest() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/profile")).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidFirstName_returnBadRequest() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", "test$")
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidLastName_returnBadRequest() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", "")
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidAddress_returnBadRequest() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", "$$$address%%%")
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfileWithInvalidTelephone_returnBadRequest() throws Exception, AnonymousUserException {
        UserProfileDTO user = getUserForProfile();
        when(userService.getUserProfile()).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", "telephone")
        ).andExpect(status().isOk());
    }

    @Test
    void updateProfile_callBusinessLogic() throws Exception {
        UserProfileDTO user = getUserForProfile();
        when(userService.updateUserProfile(any())).thenReturn(user);
        mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andExpect(status().isOk());
        verify(userService, times(1)).updateUserProfile(any());
    }

    @Test
    void updateProfile_returnUpdatedProfile() throws Exception {
        UserProfileDTO returnedUser = getUserForProfile();
        when(userService.updateUserProfile(any())).thenReturn(returnedUser);
        MvcResult result = mockMvc.perform(post("/profile")
                .param("firstName", VALID_FIRST_NAME)
                .param("lastName", VALID_LAST_NAME)
                .param("address", VALID_ADDRESS)
                .param("telephone", VALID_TELEPHONE)
        ).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_FIRST_NAME);
        Assertions.assertThat(actualContent).contains(VALID_ADDRESS);
        Assertions.assertThat(actualContent).contains(VALID_TELEPHONE);
    }

    @Test
    void getUserOrdersByProfile_returnStatusOk() throws Exception, AnonymousUserException {
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = getOrdersAndPages();
        when(orderService.getUserOrdersByPage(eq(PAGE))).thenReturn(ordersAndPages);
        mockMvc.perform(get("/profile/orders")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserOrdersByProfile_callBusinessLogic() throws AnonymousUserException, Exception {
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = getOrdersAndPages();
        when(orderService.getUserOrdersByPage(eq(PAGE))).thenReturn(ordersAndPages);
        mockMvc.perform(get("/profile/orders")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
        verify(orderService, times(1)).getUserOrdersByPage(eq(PAGE));
    }

    private UserProfileDTO getUserForProfile() {
        UserProfileDTO user = new UserProfileDTO();
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setAddress(VALID_ADDRESS);
        user.setTelephone(VALID_TELEPHONE);
        return user;
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

}
