package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/items")
public class ItemController {

    public static final String DELETE_ITEM_FAIL_MESSAGE = "This item doesnt exist.";
    private final ItemService itemService;

    public ItemController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public String getItems(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        List<ItemsDTO> items = itemService.getItemsByPage(page);
        model.addAttribute("items", items);
        int pages = itemService.getCountOfPages();
        model.addAttribute("pages", pages);
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
            return "redirect:/welcome?message=" + DELETE_ITEM_FAIL_MESSAGE;
        }
        return "redirect:/items";
    }

    @PostMapping("/{id}/copy")
    public String copyItem(
            @PathVariable Long id
    ) {
        itemService.copyItemById(id);
        return "redirect:/items";
    }

}
