package com.gmail.yauhenizhukovich.app.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import com.gmail.yauhenizhukovich.app.service.util.ItemConversionUtil;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.util.ItemConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.ItemConversionUtil.convertDatabaseObjectToItemDTO;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ITEMS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {this.itemRepository = itemRepository;}

    @Override
    public List<ItemsDTO> getItemsByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ITEMS_BY_PAGE);
        List<Item> items = itemRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_ITEMS_BY_PAGE);
        return items.stream()
                .map(ItemConversionUtil::convertDatabaseObjectToItemsDTO)
                .collect(Collectors.toList());

    }

    @Override
    public int getCountOfPages() {
        Long countOfItems = itemRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfItems, COUNT_OF_ITEMS_BY_PAGE);
    }

    @Override
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.getById(id);
        if (item == null) {
            return null;
        }
        return convertDatabaseObjectToItemDTO(item);
    }

    @Override
    public boolean deleteItemById(Long id) {
        Item item = itemRepository.getById(id);
        if (item == null) {
            return false;
        }
        return itemRepository.delete(item);
    }

    @Override
    public ItemDTO copyItemById(Long id) {
        Item item = itemRepository.getById(id);
        if (item == null) {
            return null;
        }
        Item copiedItem = getItemCopy(item);
        itemRepository.add(copiedItem);
        return convertDatabaseObjectToItemDTO(copiedItem);
    }

    @Override
    public List<ItemsDTO> getAllItems() {
        List<Item> items = itemRepository.getAll();
        return items.stream()
                .map(ItemConversionUtil::convertDatabaseObjectToItemsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO addItem(AddItemDTO itemDTO) {
        Item item = convertDTOToDatabaseObject(itemDTO);
        UUID uniqueNumber = UUID.randomUUID();
        item.setUniqueNumber(uniqueNumber.toString());
        item = itemRepository.add(item);
        return convertDatabaseObjectToItemDTO(item);
    }

    private Item getItemCopy(Item item) {
        Item copiedItem = new Item();
        copiedItem.setName(item.getName());
        UUID uniqueNumber = UUID.randomUUID();
        copiedItem.setUniqueNumber(uniqueNumber.toString());
        copiedItem.setPrice(item.getPrice());
        copiedItem.setItemDetails(item.getItemDetails());
        return copiedItem;
    }

}
