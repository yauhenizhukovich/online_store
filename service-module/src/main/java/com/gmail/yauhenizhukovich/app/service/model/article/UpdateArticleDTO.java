package com.gmail.yauhenizhukovich.app.service.model.article;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.CONTENT_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.NOT_EMPTY_CONTENT_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.NOT_EMPTY_TITLE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationMessages.TITLE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationRules.MAX_CONTENT_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.ArticleValidationRules.TITLE_PATTERN;

public class UpdateArticleDTO {

    private Long id;
    @NotEmpty(message = NOT_EMPTY_TITLE_MESSAGE)
    @Pattern(regexp = TITLE_PATTERN, message = TITLE_PATTERN_MESSAGE)
    private String title;
    @NotEmpty(message = NOT_EMPTY_CONTENT_MESSAGE)
    @Size(max = MAX_CONTENT_SIZE, message = CONTENT_SIZE_MESSAGE)
    private String content;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
