package com.gmail.yauhenizhukovich.app.web.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import static com.gmail.yauhenizhukovich.app.service.constant.FileConstant.FILE_UPLOAD_ERROR;
import static com.gmail.yauhenizhukovich.app.service.constant.FileConstant.SELECT_FILE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ITEM_FAIL_MESSAGE;

@Controller
@RequestMapping("/items")
public class ItemController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;

    public ItemController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public String getItems(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        ObjectsDTOAndPagesEntity<ItemsDTO> items = itemService.getItemsByPage(page);
        model.addAttribute("items", items.getObjects());
        model.addAttribute("pages", items.getPages());
        return "items";
    }

    @GetMapping("/{id}")
    public String getItem(
            @PathVariable Long id,
            Model model
    ) {
        ItemDTO item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/{id}")
    public String deleteItem(
            @PathVariable Long id
    ) {
        boolean isDeleted = itemService.deleteItemById(id);
        if (!isDeleted) {
            return "redirect:/?message=" + DELETE_ITEM_FAIL_MESSAGE;
        }
        return "redirect:/items";
    }

    @PostMapping("/{id}/copy")
    public String copyItem(
            @PathVariable Long id,
            @RequestParam(required = false) String name
    ) {
        if (name != null) {
            itemService.updateItemNameById(id, name);
            return "redirect:/items";
        }
        ItemDTO copiedItem = itemService.copyItemById(id);
        return "redirect:/items?copy=" + copiedItem.getUniqueNumber();
    }

    @PostMapping("/upload")
    public String uploadItemsWithXml(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", SELECT_FILE_MESSAGE);
            return "redirect:/items";
        }
        try {
            itemService.downloadItemsViaXmlFile(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Reading file error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("message", FILE_UPLOAD_ERROR);
        }
        return "redirect:/items";
    }

}
