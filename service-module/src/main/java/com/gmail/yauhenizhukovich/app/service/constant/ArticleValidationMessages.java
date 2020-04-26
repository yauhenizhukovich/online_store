package com.gmail.yauhenizhukovich.app.service.constant;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_CONTENT_SIZE;

public interface ArticleValidationMessages {

    String NOT_EMPTY_TITLE_MESSAGE = "Title cannot be empty.";
    String TITLE_PATTERN_MESSAGE = "Title can contain letters, digits and spaces";
    String NOT_EMPTY_CONTENT_MESSAGE = "Content cannot be empty.";
    String CONTENT_SIZE_MESSAGE = "Content length should be less than " + MAX_CONTENT_SIZE;

}
