package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
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

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ITEM_FAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ITEM_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.NONEXISTENT_ITEM_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DESCRIPTION;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_UNIQUE_NUMBER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
class ItemAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    @Test
    void getItems_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items")).andExpect(status().isOk());
    }

    @Test
    void getItems_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void getItems_returnItems() throws Exception {
        List<ItemsDTO> items = getItems();
        when(itemService.getAllItems()).thenReturn(items);
        MvcResult result = mockMvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(items);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void getItemById_returnStatusOk() throws Exception {
        ItemDTO item = getItem();
        when(itemService.getItemById(VALID_ID)).thenReturn(item);
        mockMvc.perform(get("/api/items/{id}", VALID_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getItemByNonexistentId_returnErrorMessage() throws Exception {
        when(itemService.getItemById(VALID_ID)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/api/items/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).isEqualTo(NONEXISTENT_ITEM_MESSAGE);
    }

    @Test
    void getItemById_callBusinessLogic() throws Exception {
        ItemDTO item = getItem();
        when(itemService.getItemById(VALID_ID)).thenReturn(item);
        mockMvc.perform(get("/api/items/{id}", VALID_ID))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItemById(VALID_ID);
    }

    @Test
    void getItemById_returnValidItem() throws Exception {
        ItemDTO item = getItem();
        when(itemService.getItemById(VALID_ID)).thenReturn(item);
        MvcResult result = mockMvc.perform(get("/api/items/{id}", VALID_ID))
                .andReturn();
        String actualContent = result.getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(item);
        Assertions.assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void addItem_returnItem() throws Exception {
        AddItemDTO addItem = new AddItemDTO();
        addItem.setName(VALID_NAME);
        addItem.setPrice(VALID_PRICE);
        addItem.setDescription(VALID_DESCRIPTION);
        String content = objectMapper.writeValueAsString(addItem);
        ItemDTO returnedItem = getItem();
        when(itemService.addItem(eq(addItem))).thenReturn(returnedItem);
        MvcResult result = mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andReturn();
        String expectedContent = objectMapper.writeValueAsString(returnedItem);
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(expectedContent).isEqualTo(actualContent);
    }

    @Test
    void deleteItemById_returnStatusOk() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", VALID_ID)).andExpect(status().isOk());
    }

    @Test
    void deleteItemById_callBusinessLogic() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", VALID_ID)).andExpect(status().isOk());
        verify(itemService, times(1)).deleteItemById(eq(VALID_ID));
    }

    @Test
    void deleteItemById_returnDeleteMessage() throws Exception {
        when(itemService.deleteItemById(VALID_ID)).thenReturn(true);
        MvcResult result = mockMvc.perform(delete("/api/items/{id}", VALID_ID)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        verify(itemService, times(1)).deleteItemById(eq(VALID_ID));
        Assertions.assertThat(actualContent).isEqualTo(DELETE_ITEM_MESSAGE);
    }

    @Test
    void deleteItemByNonexistentId_returnDeleteFailMessage() throws Exception {
        when(itemService.deleteItemById(VALID_ID)).thenReturn(false);
        MvcResult result = mockMvc.perform(delete("/api/items/{id}", VALID_ID)).andReturn();
        String actualContent = result.getResponse().getContentAsString();
        verify(itemService, times(1)).deleteItemById(eq(VALID_ID));
        Assertions.assertThat(actualContent).isEqualTo(DELETE_ITEM_FAIL_MESSAGE);
    }

    private List<ItemsDTO> getItems() {
        List<ItemsDTO> items = new ArrayList<>();
        ItemsDTO item = new ItemsDTO();
        item.setName(VALID_NAME);
        item.setUniqueNumber(VALID_UNIQUE_NUMBER);
        item.setPrice(VALID_PRICE);
        items.add(item);
        return items;
    }

    private ItemDTO getItem() {
        ItemDTO item = new ItemDTO();
        item.setPrice(VALID_PRICE);
        item.setDescription(VALID_DESCRIPTION);
        item.setUniqueNumber(VALID_UNIQUE_NUMBER);
        item.setName(VALID_NAME);
        return item;
    }

}