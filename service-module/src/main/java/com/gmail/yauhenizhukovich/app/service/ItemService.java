package com.gmail.yauhenizhukovich.app.service;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;

public interface ItemService {

    List<ItemsDTO> getItemsByPage(Integer page);

    int getCountOfPages();

    boolean deleteItemById(Long id);

    ItemDTO getItemById(Long id);

    ItemDTO copyItemById(Long id);

    List<ItemsDTO> getAllItems();

    ItemDTO addItem(AddItemDTO item);

}
