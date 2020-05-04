package com.gmail.yauhenizhukovich.app.service.constant;

import java.math.BigDecimal;
import java.util.UUID;

import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;

import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_USERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;

public interface ServiceUnitTestConstant {

    int PAGE = 2;
    int START_POSITION = getStartPositionByPageNumber(PAGE, COUNT_OF_USERS_BY_PAGE);
    Long VALID_ID = 9L;
    long COUNT_OF_OBJECTS = 4L;
    String VALID_AUTHOR_NAME = "Ivan Ivanov";
    String VALID_REVIEW_TEXT = "That's good!";
    String VALID_FIRSTNAME = "Ivan";
    String VALID_LASTNAME = "Ivanovich";
    String VALID_PATRONYMIC = "Ivanov";
    String VALID_EMAIL = "randomemail@gmail.com";
    String VALID_PASSWORD = "test";
    String VALID_UNIQUE_NUMBER = UUID.randomUUID().toString();
    RoleEnumService VALID_ROLE = RoleEnumService.SECURE_API_USER;
    String VALID_AUTHOR_FIRSTNAME = "Petr";
    String VALID_AUTHOR_LASTNAME = "Petrov";
    long VALID_COMMENT_ID = 2L;
    String VALID_CONTENT = "Article content.";
    String VALID_RUNDOWN = "Just rundown.";
    String VALID_TITLE = "My new article";
    String TO = "test@gmail.com";
    String SUBJECT = "Title";
    String TEXT = "Text";
    String VALID_DESCRIPTION = "Very tasty";
    String VALID_NAME = "Milk";
    BigDecimal VALID_PRICE = BigDecimal.valueOf(3.15);
    Integer VALID_AMOUNT = 5;
    String VALID_ADDRESS = "Brest, ul. Moldavskih Partizan, d.13";
    String VALID_TELEPHONE = "8029211111";

}
