package com.gmail.yauhenizhukovich.app.service.util.conversion;

import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.ItemDetails;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;

public class ItemConversionUtil {

    public static ItemsDTO convertDatabaseObjectToItemsDTO(Item item) {
        ItemsDTO itemDTO = new ItemsDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

    public static ItemDTO convertDatabaseObjectToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(item.getName());
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        itemDTO.setPrice(item.getPrice());
        if (item.getItemDetails() != null) {
            itemDTO.setDescription(item.getItemDetails().getDescription());
        }
        return itemDTO;
    }

    public static Item convertDTOToDatabaseObject(AddItemDTO itemDTO) {
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setDescription(itemDTO.getDescription());
        itemDetails.setItem(item);
        item.setItemDetails(itemDetails);
        return item;
    }

}
