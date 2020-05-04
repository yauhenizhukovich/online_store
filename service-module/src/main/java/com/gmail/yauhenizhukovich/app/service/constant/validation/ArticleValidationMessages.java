package com.gmail.yauhenizhukovich.app.service.constant.validation;

public interface ArticleValidationMessages {

    String NOT_EMPTY_TITLE_MESSAGE = "Title cannot be empty.";
    String TITLE_PATTERN_MESSAGE = "Title can contain letters, digits, spaces and commas.";
    String NOT_EMPTY_CONTENT_MESSAGE = "Content cannot be empty.";
    String CONTENT_SIZE_MESSAGE = "Content length should be less than " + ArticleValidationRules.MAX_CONTENT_SIZE;

}
