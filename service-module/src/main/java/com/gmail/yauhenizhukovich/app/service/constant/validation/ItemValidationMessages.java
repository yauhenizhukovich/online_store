package com.gmail.yauhenizhukovich.app.service.constant.validation;

public interface ItemValidationMessages {

    String NOT_EMPTY_ITEM_NAME_MESSAGE = "Item name cannot be empty.";
    String ITEM_NAME_PATTERN_MESSAGE = "Name can contain letters, digits and spaces";
    String NOT_EMPTY_ITEM_DESCRIPTION_MESSAGE = "Description cannot be empty.";
    String ITEM_DESCRIPTION_SIZE_MESSAGE = "Description length should be less than " + ItemValidationRules.MAX_ITEM_DESCRIPTION_SIZE;
    String PRICE_FORMAT_MESSAGE = "Price can contain 4 digits before the decimal point and 3 after it";
    String NOT_NULL_PRICE_MESSAGE = "Price cannot be empty";
    String MAX_PRICE_MESSAGE = "Price should be less than " + ItemValidationRules.MAX_PRICE;
    String MIN_PRICE_MESSAGE = "Price should be more than " + ItemValidationRules.MIN_PRICE;

}
