package com.gmail.yauhenizhukovich.app.web.controller.constant;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gmail.yauhenizhukovich.app.repository.model.StatusEnum;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;

import static com.gmail.yauhenizhukovich.app.repository.model.StatusEnum.NEW;

public interface TestConstant {

    int PAGE = 3;
    Integer PAGES = 7;
    String VALID_COMMENT_CONTENT = "Comment content.";
    LocalDate VALID_DATE = LocalDate.now();
    Long VALID_ID = 3L;
    String VALID_TITLE = "Good article";
    String VALID_AUTHOR_FIRSTNAME = "Eugene";
    String VALID_AUTHOR_LASTNAME = "Moria";
    String VALID_RUNDOWN = "This is the test content of article.";
    String VALID_CONTENT = "This is the test content of article.";
    String VALID_ADDRESS = "Brest, ul. Sovietskaya, 23";
    String VALID_FIRST_NAME = "Misha";
    String VALID_LAST_NAME = "Mihailov";
    String VALID_TELEPHONE = "80292258422";
    String VALID_PATRONYMIC = "Petrovich";
    String VALID_EMAIL = "sidorov@gmail.com";
    String VALID_UNIQUE_NUMBER = "testUniqueNumber";
    RoleEnumService VALID_ROLE = RoleEnumService.SALE_USER;
    String VALID_FULL_NAME = "Ivan Ivanov";
    String VALID_REVIEW_TEXT = "This is test review example.";
    boolean VALID_ACTIVE = true;
    StatusEnum VALID_STATUS = NEW;
    int VALID_AMOUNT = 5;
    String VALID_NAME = "Milk";
    BigDecimal VALID_PRICE = BigDecimal.valueOf(3.00);
    String VALID_DESCRIPTION = "Tastiest milk ever!";

}
