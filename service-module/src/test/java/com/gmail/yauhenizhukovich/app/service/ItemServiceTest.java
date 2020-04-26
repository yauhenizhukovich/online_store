package com.gmail.yauhenizhukovich.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.ItemDetails;
import com.gmail.yauhenizhukovich.app.service.impl.ItemServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import com.gmail.yauhenizhukovich.app.service.util.ItemConversionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ARTICLES_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ITEMS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_USERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private static final long COUNT_OF_OBJECTS = 13L;
    private static final int PAGE = 2;
    private static final int START_POSITION = getStartPositionByPageNumber(PAGE, COUNT_OF_ITEMS_BY_PAGE);
    private static final String VALID_DESCRIPTION = "Very tasty";
    private static final Long VALID_ID = 7L;
    private static final String VALID_NAME = "Milk";
    private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(3.15);
    private static final String VALID_UNIQUE_NUMBER = UUID.randomUUID().toString();
    @Mock
    private ItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    public void setup() {
        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    public void getItemsByPage_returnItems() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_ARTICLES_BY_PAGE))
                .thenReturn(returnedItems);
        List<ItemsDTO> actualItems = itemService.getItemsByPage(PAGE);
        Assertions.assertThat(actualItems).isNotNull();
        verify(itemRepository, times(1))
                .getObjectsByStartPositionAndMaxResult(START_POSITION, COUNT_OF_USERS_BY_PAGE);
        ItemsDTO actualItem = actualItems.get(0);
        Item returnedItem = returnedItems.get(0);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void getCountOfPages_returnPages() {
        when(itemRepository.getCountOfObjects()).thenReturn(COUNT_OF_OBJECTS);
        int pages = itemService.getCountOfPages();
        verify(itemRepository, times(1)).getCountOfObjects();
        Assertions.assertThat(pages).isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ITEMS_BY_PAGE));
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
    public void copyItemById_returnCopiedItem() {
        Item returnedItem = getItem();
        when(itemRepository.getById(VALID_ID)).thenReturn(returnedItem);
        when(itemRepository.add(returnedItem)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.copyItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
        verify(itemRepository, times(1)).add(returnedItem);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(returnedItem.getItemDetails().getDescription());
    }

    @Test
    public void copyItemById_returnNull() {
        when(itemRepository.getById(VALID_ID)).thenReturn(null);
        ItemDTO actualItem = itemService.copyItemById(VALID_ID);
        Assertions.assertThat(actualItem).isNull();
        verify(itemRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void getAllItems_returnItems() {
        List<Item> returnedItems = getItems();
        when(itemRepository.getAll()).thenReturn(returnedItems);
        List<ItemsDTO> actualItems = itemService.getAllItems();
        Assertions.assertThat(actualItems).isNotNull();
        verify(itemRepository, times(1)).getAll();
        ItemsDTO actualItem = actualItems.get(0);
        Item returnedItem = returnedItems.get(0);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(returnedItem.getPrice());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
    }

    @Test
    public void addItem_returnItem() {
        AddItemDTO addedItemDTO = new AddItemDTO();
        Item addedItem = ItemConversionUtil.convertDTOToDatabaseObject(addedItemDTO);
        Item returnedItem = getItem();
        when(itemRepository.add(addedItem)).thenReturn(returnedItem);
        ItemDTO actualItem = itemService.addItem(addedItemDTO);
        Assertions.assertThat(actualItem).isNotNull();
        verify(itemRepository, times(1)).add(addedItem);
        Assertions.assertThat(actualItem.getName()).isEqualTo(returnedItem.getName());
        Assertions.assertThat(actualItem.getUniqueNumber()).isEqualTo(returnedItem.getUniqueNumber());
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(returnedItem.getItemDetails().getDescription());
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
