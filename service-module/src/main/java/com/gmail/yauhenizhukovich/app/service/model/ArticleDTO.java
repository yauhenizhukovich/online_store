package com.gmail.yauhenizhukovich.app.service.model;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.CONTENT_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.NOT_EMPTY_CONTENT_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.NOT_EMPTY_RUNDOWN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.NOT_EMPTY_TITLE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.RUNDOWN_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationMessages.TITLE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_CONTENT_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.MAX_RUNDOWN_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.ArticleValidationRules.TITLE_PATTERN;

public class ArticleDTO {

    private LocalDate date;
    @NotEmpty(message = NOT_EMPTY_TITLE_MESSAGE)
    @Pattern(regexp = TITLE_PATTERN, message = TITLE_PATTERN_MESSAGE)
    private String title;
    private String authorFirstName;
    private String authorLastName;
    @NotEmpty(message = NOT_EMPTY_RUNDOWN_MESSAGE)
    @Size(max = MAX_RUNDOWN_SIZE, message = RUNDOWN_SIZE_MESSAGE)
    private String rundown;
    @NotEmpty(message = NOT_EMPTY_CONTENT_MESSAGE)
    @Size(max = MAX_CONTENT_SIZE, message = CONTENT_SIZE_MESSAGE)
    private String content;
    private List<CommentDTO> comments;
    private Long id;

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setRundown(String rundown) {
        this.rundown = rundown;
    }

    public String getRundown() {
        return rundown;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
