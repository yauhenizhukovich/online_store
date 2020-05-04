package com.gmail.yauhenizhukovich.app.service.model.order;

import com.gmail.yauhenizhukovich.app.repository.model.StatusEnum;

public class UpdateOrderDTO {

    private Long id;
    private StatusEnum status;

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
