package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
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
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.PAGES;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_DESCRIPTION;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.web.controller.constant.TestConstant.VALID_UNIQUE_NUMBER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@Import(UnitTestConfig.class)
@WithMockUser(roles = {"SALE_USER", "CUSTOMER_USER"})
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @Test
    void getItems_returnStatusOk() throws Exception {
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = getItemsAndPages();
        when(itemService.getItemsByPage(1)).thenReturn(itemsAndPages);
        mockMvc.perform(get("/items")).andExpect(status().isOk());
    }

    @Test
    void getItems_callBusinessLogic() throws Exception {
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = getItemsAndPages();
        when(itemService.getItemsByPage(eq(PAGE))).thenReturn(itemsAndPages);
        mockMvc.perform(get("/items")
                .param("page", String.valueOf(PAGE)))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItemsByPage(eq(PAGE));
    }

    @Test
    void getItems_returnItems() throws Exception {
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = getItemsAndPages();
        when(itemService.getItemsByPage(eq(PAGE))).thenReturn(itemsAndPages);
        MvcResult result = mockMvc.perform(get("/items")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(PAGE)))
                .andReturn();
        verify(itemService, times(1)).getItemsByPage(eq(PAGE));
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_NAME);
        Assertions.assertThat(actualContent).contains(String.valueOf(VALID_PRICE));
    }

    @Test
    void getItem_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items/{id}", VALID_ID)).andExpect(status().isOk());
    }

    @Test
    void getItem_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/items/{id}", VALID_ID)).andExpect(status().isOk());
        verify(itemService, times(1)).getItemById(VALID_ID);
    }

    @Test
    void getItem_returnItem() throws Exception {
        ItemDTO item = getItem();
        when(itemService.getItemById(VALID_ID)).thenReturn(item);
        MvcResult result = mockMvc.perform(get("/items/{id}", VALID_ID)).andReturn();
        verify(itemService, times(1)).getItemById(VALID_ID);
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains(VALID_NAME);
        Assertions.assertThat(actualContent).contains(VALID_UNIQUE_NUMBER);
        Assertions.assertThat(actualContent).contains(VALID_DESCRIPTION);
        Assertions.assertThat(actualContent).contains(String.valueOf(VALID_PRICE));
    }

    @Test
    void getItemByNonexistentId_returnEmptyPage() throws Exception {
        when(itemService.getItemById(VALID_ID)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/items/{id}", VALID_ID)).andReturn();
        verify(itemService, times(1)).getItemById(VALID_ID);
        String actualContent = result.getResponse().getContentAsString();
        Assertions.assertThat(actualContent).contains("THIS ITEM DOESNT EXIST");
    }

    @Test
    void deleteItem_returnRedirectedUrl() throws Exception {
        when(itemService.deleteItemById(VALID_ID)).thenReturn(true);
        mockMvc.perform(post("/items/{id}", VALID_ID)).andExpect(redirectedUrl("/items"));
    }

    @Test
    void deleteItem_callBusinessLogic() throws Exception {
        when(itemService.deleteItemById(VALID_ID)).thenReturn(true);
        mockMvc.perform(post("/items/{id}", VALID_ID)).andExpect(redirectedUrl("/items"));
        verify(itemService, times(1)).deleteItemById(VALID_ID);
    }

    @Test
    void deleteItemByNonexistentId_returnStartPageWithMessage() throws Exception {
        when(itemService.deleteItemById(VALID_ID)).thenReturn(false);
        mockMvc.perform(post("/items/{id}", VALID_ID))
                .andExpect(redirectedUrl("/?message=" + DELETE_ITEM_FAIL_MESSAGE));
        verify(itemService, times(1)).deleteItemById(VALID_ID);
    }

    @Test
    void copyItem_returnRedirectedUrl() throws Exception {
        ItemDTO item = getItem();
        when(itemService.copyItemById(VALID_ID)).thenReturn(item);
        mockMvc.perform(post("/items/{id}/copy", VALID_ID))
                .andExpect(redirectedUrl("/items?copy=" + VALID_UNIQUE_NUMBER));
    }

    @Test
    void copyItemWithName_returnRedirectedUrl() throws Exception {
        ItemDTO item = getItem();
        when(itemService.updateItemNameById(VALID_ID, "Sugar")).thenReturn(item);
        mockMvc.perform(post("/items/{id}/copy", VALID_ID)
                .param("name", "Sugar"))
                .andExpect(redirectedUrl("/items"));
    }

    @Test
    void copyItem_callBusinessLogic() throws Exception {
        ItemDTO item = getItem();
        when(itemService.copyItemById(VALID_ID)).thenReturn(item);
        mockMvc.perform(post("/items/{id}/copy", VALID_ID))
                .andExpect(redirectedUrl("/items?copy=" + VALID_UNIQUE_NUMBER));
        verify(itemService, times(1)).copyItemById(eq(VALID_ID));
    }

    @Test
    void copyItemWithName_callBusinessLogic() throws Exception {
        ItemDTO item = getItem();
        when(itemService.updateItemNameById(VALID_ID, "Sugar")).thenReturn(item);
        mockMvc.perform(post("/items/{id}/copy", VALID_ID)
                .param("name", "Sugar"))
                .andExpect(redirectedUrl("/items"));
        verify(itemService, times(1)).updateItemNameById(eq(VALID_ID), eq("Sugar"));
    }

    private ObjectsDTOAndPagesEntity<ItemsDTO> getItemsAndPages() {
        List<ItemsDTO> items = new ArrayList<>();
        ItemsDTO item = new ItemsDTO();
        item.setName(VALID_NAME);
        item.setUniqueNumber(VALID_UNIQUE_NUMBER);
        item.setPrice(VALID_PRICE);
        items.add(item);
        return new ObjectsDTOAndPagesEntity<>(PAGES, items);
    }

    private ItemDTO getItem() {
        ItemDTO item = new ItemDTO();
        item.setName(VALID_NAME);
        item.setUniqueNumber(VALID_UNIQUE_NUMBER);
        item.setDescription(VALID_DESCRIPTION);
        item.setPrice(VALID_PRICE);
        return item;
    }

}