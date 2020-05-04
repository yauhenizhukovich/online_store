package com.gmail.yauhenizhukovich.app.service.model.article;

import java.time.LocalDate;

public class ArticlesDTO {

    private Long id;
    private LocalDate date;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String rundown;

    public String getRundown() {
        return rundown;
    }

    public void setRundown(String rundown) {
        this.rundown = rundown;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
