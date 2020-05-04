package com.gmail.yauhenizhukovich.app.service.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.ItemDetails;
import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.ItemConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import static com.gmail.yauhenizhukovich.app.service.constant.FileConstant.UPLOADED_FOLDER;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ITEMS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ItemConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.ItemConversionUtil.convertDatabaseObjectToItemDTO;
import static com.gmail.yauhenizhukovich.app.service.util.reading.XmlReadingUtil.readItems;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {this.itemRepository = itemRepository;}

    @Override
    public ItemDTO addItem(AddItemDTO itemDTO) {
        Item item = convertDTOToDatabaseObject(itemDTO);
        UUID uniqueNumber = UUID.randomUUID();
        item.setUniqueNumber(uniqueNumber.toString());
        item = itemRepository.add(item);
        return convertDatabaseObjectToItemDTO(item);
    }

    @Override
    public List<ItemsDTO> getAllItems() {
        List<Item> items = itemRepository.getAll();
        return getItemsDTO(items);
    }

    @Override
    public ObjectsDTOAndPagesEntity<ItemsDTO> getItemsByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ITEMS_BY_PAGE);
        List<Item> items = itemRepository.getPaginatedObjects(startPosition, COUNT_OF_ITEMS_BY_PAGE);
        int pages = getPages();
        List<ItemsDTO> itemsDTO = getItemsDTO(items);
        return new ObjectsDTOAndPagesEntity<>(pages, itemsDTO);
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
    public ItemDTO updateItemNameById(Long id, String name) {
        Item item = itemRepository.getById(id);
        if (item == null) {
            return null;
        }
        item.setName(name);
        return convertDatabaseObjectToItemDTO(item);
    }

    @Override
    public List<ItemsDTO> downloadItemsViaXmlFile(MultipartFile file) throws IOException, ParserConfigurationException, SAXException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);
        List<AddItemDTO> itemsDTO = readItems(file.getOriginalFilename());
        logger.debug("Xml file successfully read.");
        List<Item> items = itemsDTO.stream()
                .map(ItemConversionUtil::convertDTOToDatabaseObject)
                .peek(item -> {
                    String uniqueNumber = UUID.randomUUID().toString();
                    item.setUniqueNumber(uniqueNumber);
                })
                .map(itemRepository::add)
                .collect(Collectors.toList());
        return getItemsDTO(items);
    }

    private List<ItemsDTO> getItemsDTO(List<Item> items) {
        return items.stream()
                .map(ItemConversionUtil::convertDatabaseObjectToItemsDTO)
                .collect(Collectors.toList());
    }

    private int getPages() {
        Long countOfItems = itemRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfItems, COUNT_OF_ITEMS_BY_PAGE);
    }

    private Item getItemCopy(Item item) {
        Item copiedItem = new Item();
        UUID uniqueNumber = UUID.randomUUID();
        copiedItem.setUniqueNumber(uniqueNumber.toString());
        copiedItem.setPrice(item.getPrice());
        if (item.getItemDetails() != null) {
            String description = item.getItemDetails().getDescription();
            ItemDetails itemDetails = new ItemDetails();
            itemDetails.setDescription(description);
            itemDetails.setItem(copiedItem);
            copiedItem.setItemDetails(itemDetails);
        }
        return copiedItem;
    }

}