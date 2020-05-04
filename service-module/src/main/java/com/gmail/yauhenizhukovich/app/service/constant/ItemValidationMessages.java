package com.gmail.yauhenizhukovich.app.service.constant;

import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MAX_ITEM_DESCRIPTION_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MAX_PRICE;
import static com.gmail.yauhenizhukovich.app.service.constant.ItemValidationRules.MIN_PRICE;

public interface ItemValidationMessages {

    String NOT_EMPTY_ITEM_NAME_MESSAGE = "Item cannot be empty.";
    String ITEM_NAME_PATTERN_MESSAGE = "Name can contain letters, digits and spaces";
    String NOT_EMPTY_ITEM_DESCRIPTION_MESSAGE = "Description cannot be empty.";
    String ITEM_DESCRIPTION_SIZE_MESSAGE = "Description length should be less than " + MAX_ITEM_DESCRIPTION_SIZE;
    String PRICE_FORMAT_MESSAGE = "Price can contain 4 digits before the decimal point and 3 after it";
    String NOT_NULL_PRICE_MESSAGE = "Price cannot be empty";
    String MAX_PRICE_MESSAGE = "Price should be less than " + MAX_PRICE;
    String MIN_PRICE_MESSAGE = "Price should be more than " + MIN_PRICE;

}
