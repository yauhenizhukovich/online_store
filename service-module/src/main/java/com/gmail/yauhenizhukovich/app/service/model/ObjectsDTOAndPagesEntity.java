package com.gmail.yauhenizhukovich.app.service.model;

import java.util.List;

public class ObjectsDTOAndPagesEntity<T> {

    private int pages;
    private List<T> objects;

    public ObjectsDTOAndPagesEntity(int pages, List<T> objects) {
        this.pages = pages;
        this.objects = objects;
    }

    public int getPages() {
        return pages;
    }

    public List<T> getObjects() {
        return objects;
    }

}
