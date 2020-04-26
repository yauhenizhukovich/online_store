package com.gmail.yauhenizhukovich.app.service.model.article;

import java.time.LocalDate;
import java.util.List;

public class ArticleDTO {

    private Long id;
    private LocalDate date;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String content;
    private List<CommentDTO> comments;

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
