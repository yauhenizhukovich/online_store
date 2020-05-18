package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.ItemService;
import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemDTO;
import com.gmail.yauhenizhukovich.app.service.model.item.ItemsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ITEM_FAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.DELETE_ITEM_MESSAGE;
import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.NONEXISTENT_ITEM_MESSAGE;

@RestController
@RequestMapping("/api/items")
public class ItemAPIController {


    private final ItemService itemService;

    public ItemAPIController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public List<ItemsDTO> getItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Object getItem(
            @PathVariable Long id
    ) {
        ItemDTO item = itemService.getItemById(id);
        if (item == null) {
            return NONEXISTENT_ITEM_MESSAGE;
        }
        return item;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Object addItem(
            @RequestBody @Valid AddItemDTO item,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            List<ObjectError> bindingResultErrors = bindingResult.getAllErrors();
            bindingResultErrors.forEach(error -> errors.add(error.getDefaultMessage()));
            return errors;
        }
        return itemService.addItem(item);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(
            @PathVariable Long id
    ) {
        boolean isDeleted = itemService.deleteItemById(id);
        if (!isDeleted) {
            return DELETE_ITEM_FAIL_MESSAGE;
        }
        return DELETE_ITEM_MESSAGE;
    }

}
