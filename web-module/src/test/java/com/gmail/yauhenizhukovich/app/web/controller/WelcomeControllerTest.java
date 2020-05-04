package com.gmail.yauhenizhukovich.app.web.controller;

import com.gmail.yauhenizhukovich.app.web.controller.config.UnitTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WelcomeController.class)
@Import(UnitTestConfig.class)
class WelcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWelcomePage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoginPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

}