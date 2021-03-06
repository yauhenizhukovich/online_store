package com.gmail.yauhenizhukovich.app.service.model.order;

import java.math.BigDecimal;

import com.gmail.yauhenizhukovich.app.repository.model.StatusEnum;

public class OrderDTO {

    private Long id;
    private StatusEnum status;
    private String itemName;
    private String customerUniqueNumber;
    private String customerTelephone;
    private int amount;
    private BigDecimal price;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setCustomerUniqueNumber(String customerUniqueNumber) {
        this.customerUniqueNumber = customerUniqueNumber;
    }

    public String getCustomerUniqueNumber() {
        return customerUniqueNumber;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
