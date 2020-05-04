package com.gmail.yauhenizhukovich.app.service.model.article;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.CONTENT_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.NOT_EMPTY_CONTENT_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.NOT_EMPTY_TITLE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.TITLE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_CONTENT_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.TITLE_PATTERN;

public class AddArticleDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotEmpty(message = NOT_EMPTY_TITLE_MESSAGE)
    @Pattern(regexp = TITLE_PATTERN, message = TITLE_PATTERN_MESSAGE)
    private String title;
    @NotEmpty(message = NOT_EMPTY_CONTENT_MESSAGE)
    @Size(max = MAX_CONTENT_SIZE, message = CONTENT_SIZE_MESSAGE)
    private String content;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
