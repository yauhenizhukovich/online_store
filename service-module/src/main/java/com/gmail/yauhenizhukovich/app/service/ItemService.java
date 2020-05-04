package com.gmail.yauhenizhukovich.app.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

public interface ItemService {

    ObjectsDTOAndPagesEntity<ItemsDTO> getItemsByPage(Integer page);

    boolean deleteItemById(Long id);

    ItemDTO getItemById(Long id);

    ItemDTO copyItemById(Long id);

    List<ItemsDTO> getAllItems();

    ItemDTO addItem(AddItemDTO item);

    ItemDTO updateItemNameById(Long id, String name);

    List<ItemsDTO> downloadItemsViaXmlFile(MultipartFile originalFilename) throws IOException, ParserConfigurationException, SAXException;

}
