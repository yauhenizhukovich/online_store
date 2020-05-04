package com.gmail.yauhenizhukovich.app.service.model.item;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.ITEM_DESCRIPTION_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.ITEM_NAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.MAX_PRICE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.MIN_PRICE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.NOT_EMPTY_ITEM_DESCRIPTION_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.NOT_EMPTY_ITEM_NAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.NOT_NULL_PRICE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationMessages.PRICE_FORMAT_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.ITEM_NAME_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MAX_ITEM_DESCRIPTION_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MAX_PRICE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MIN_PRICE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.PRICE_FRACTION_PART_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.PRICE_INTEGER_PART_SIZE;

public class AddItemDTO {

    @NotEmpty(message = NOT_EMPTY_ITEM_NAME_MESSAGE)
    @Pattern(regexp = ITEM_NAME_PATTERN, message = ITEM_NAME_PATTERN_MESSAGE)
    private String name;
    @NotNull(message = NOT_NULL_PRICE_MESSAGE)
    @Digits(integer = PRICE_INTEGER_PART_SIZE, fraction = PRICE_FRACTION_PART_SIZE, message = PRICE_FORMAT_MESSAGE)
    @DecimalMin(value = MIN_PRICE, inclusive = false, message = MIN_PRICE_MESSAGE)
    @DecimalMax(value = MAX_PRICE, message = MAX_PRICE_MESSAGE)
    private BigDecimal price;
    @NotEmpty(message = NOT_EMPTY_ITEM_DESCRIPTION_MESSAGE)
    @Size(max = MAX_ITEM_DESCRIPTION_SIZE, message = ITEM_DESCRIPTION_SIZE_MESSAGE)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
