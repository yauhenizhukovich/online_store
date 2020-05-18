package com.gmail.yauhenizhukovich.app.service;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.ItemDetails;
import com.gmail.yauhenizhukovich.app.service.impl.ItemServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.ItemConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ITEMS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.COUNT_OF_OBJECTS;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.START_POSITION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_DESCRIPTION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_UNIQUE_NUMBER;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    public void setup() {
        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    public void addItem_callDatabase() {
        AddItemDTO addedItemDTO = new AddItemDTO();
        Item addedItem = ItemConversionUtil.convertDTOToDatabaseObject(addedItemDTO);
        Item returnedItem = getItem();
        when(itemRepository.add(addedItem)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.addItem(addedItemDTO);
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).add(addedItem);
    }

    @Test
    public void addItem_returnItemCheckNameUniqueNumber() {
        AddItemDTO addedItemDTO = new AddItemDTO();
        Item addedItem = ItemConversionUtil.convertDTOToDatabaseObject(addedItemDTO);
        Item returnedItem = getItem();
        when(itemRepository.add(addedItem)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.addItem(addedItemDTO);
        Assertions.assertThat(actualItem).isNotNull();
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void addItem_returnItemCheckDescriptionPrice() {
        AddItemDTO addedItemDTO = new AddItemDTO();
        Item addedItem = ItemConversionUtil.convertDTOToDatabaseObject(addedItemDTO);
        Item returnedItem = getItem();
        when(itemRepository.add(addedItem)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.addItem(addedItemDTO);
        Assertions.assertThat(actualItem).isNotNull();
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(returnedItem.getItemDetails().getDescription());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
    }

    @Test
    public void getAllItems_callDatabase() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getAll()).thenReturn(returnedItems);
        List<ItemsDTO> actualItems = itemService.getAllItems();
        Assertions.assertThat(actualItems).isNotNull();
        verify(itemRepository, times(1)).getAll();
    }

    @Test
    public void getAllItems_returnItems() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getAll()).thenReturn(returnedItems);
        List<ItemsDTO> actualItems = itemService.getAllItems();
        Assertions.assertThat(actualItems).isNotNull();
        ItemsDTO actualItem = actualItems.get(0);
        Item returnedItem = returnedItems.get(0);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void getItemsByPage_callDatabase() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ITEMS_BY_PAGE))
                .thenReturn(returnedItems);
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = itemService.getItemsByPage(PAGE);
        List<ItemsDTO> actualItems = itemsAndPages.getObjects();
        Assertions.assertThat(actualItems).isNotNull();
        verify(itemRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_ITEMS_BY_PAGE);
    }

    @Test
    public void getItemsByPage_returnItems() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ITEMS_BY_PAGE))
                .thenReturn(returnedItems);
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = itemService.getItemsByPage(PAGE);
        List<ItemsDTO> actualItems = itemsAndPages.getObjects();
        Assertions.assertThat(actualItems).isNotNull();
        verify(itemRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_ITEMS_BY_PAGE);
        ItemsDTO actualItem = actualItems.get(0);
        Item returnedItem = returnedItems.get(0);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void getItemsByPage_returnPages() {
        when(itemRepository.getCountOfObjects())
                .thenReturn(COUNT_OF_OBJECTS);
        ObjectsDTOAndPagesEntity<ItemsDTO> itemsAndPages = itemService.getItemsByPage(PAGE);
        Assertions.assertThat(itemsAndPages).isNotNull();
        verify(itemRepository, times(1))
                .getCountOfObjects();
        Assertions.assertThat(itemsAndPages.getPages())
                .isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ITEMS_BY_PAGE));
    }

    @Test
    public void getItemById_returnItem() {
        Item returnedItem = getItem();
        when(itemRepository.getById(VALID_ID)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.getItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(returnedItem.getItemDetails().getDescription());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void getItemByNonexistentId_returnNull() {
        when(itemRepository.getById(VALID_ID)).thenReturn(null);
        ItemDTO actualItem = itemService.getItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void deleteItemById_returnTrue() {
        Item returnedItem = getItem();
        when(itemRepository.getById(VALID_ID)).thenReturn(returnedItem);
        when(itemRepository.delete(returnedItem)).thenReturn(true);
        boolean isDeleted = itemService.deleteItemById(VALID_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteItemByNonexistentId_returnFalse() {
        when(itemRepository.getById(VALID_ID)).thenReturn(null);
        boolean isDeleted = itemService.deleteItemById(VALID_ID);
        Assertions.assertThat(isDeleted).isFalse();
    }

    @Test
    public void copyItemById_returnCopiedItem() {
        Item returnedItem = getItem();
        when(itemRepository.getById(VALID_ID)).thenReturn(returnedItem);
        when(itemRepository.add(any())).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.copyItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
        verify(itemRepository, times(1)).add(any());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(returnedItem.getItemDetails().getDescription());
    }

    @Test
    public void copyItemByNonexistentId_returnNull() {
        when(itemRepository.getById(VALID_ID)).thenReturn(null);
        ItemDTO actualItem = itemService.copyItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void updateItemNameById_returnItem() {
        Item returnedItem = getItem();
        when(itemRepository.getById(VALID_ID)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.updateItemNameById(VALID_ID, "test");
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getName()).isEqualTo("test");
    }

    @Test
    public void updateItemNameByNonexistentId_returnNull() {
        when(itemRepository.getById(VALID_ID)).thenReturn(null);
        ItemDTO actualItem = itemService.updateItemNameById(VALID_ID, VALID_NAME);
        Assertions.assertThat(actualItem).isNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
    }

    private Item getItem() {
        Item item = new Item();
        item.setName(VALID_NAME);
        item.setUniqueNumber(VALID_UNIQUE_NUMBER);
        item.setPrice(VALID_PRICE);
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setDescription(VALID_DESCRIPTION);
        item.setItemDetails(itemDetails);
        return item;
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(getItem());
        return items;
    }

}
