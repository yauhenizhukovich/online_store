package com.gmail.yauhenizhukovich.app.service.constant;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_CONTENT_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_RUNDOWN_SIZE;

public interface ArticleValidationMessages {

    String RUNDOWN_SIZE_MESSAGE = "Rundown length should be less than " + MAX_RUNDOWN_SIZE;
    String CONTENT_SIZE_MESSAGE = "Content length should be less than " + MAX_CONTENT_SIZE;
    String NOT_EMPTY_TITLE_MESSAGE = "Title cannot be empty.";
    String TITLE_PATTERN_MESSAGE = "Title can contain letters, digits and spaces";
    String NOT_EMPTY_RUNDOWN_MESSAGE = "Rundown cannot be empty.";
    String NOT_EMPTY_CONTENT_MESSAGE = "Content cannot be empty.";

}
