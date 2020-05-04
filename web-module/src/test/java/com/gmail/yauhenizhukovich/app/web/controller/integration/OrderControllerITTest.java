package com.gmail.yauhenizhukovich.app.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
public class OrderControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(statements = "INSERT INTO ordering (date, status, amount, price, ordered_item_id, customer_id) VALUES ('2020-05-03','NEW',5,10,1,2)")
    public void getOrder_returnOrder() throws Exception {
        mockMvc.perform(get("/orders/2")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("NEW")))
                .andExpect(content().string(containsString("5")));
    }

    @Test
    public void getOrders_returnOrders() throws Exception {
        mockMvc.perform(get("/orders")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("New")))
                .andExpect(content().string(containsString("5")));
    }

}
