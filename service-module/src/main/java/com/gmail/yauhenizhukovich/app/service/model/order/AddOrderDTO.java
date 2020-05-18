package com.gmail.yauhenizhukovich.app.service.model.order;

import java.util.Objects;

public class AddOrderDTO {

    private int amount;
    private String itemUniqueNumber;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemUniqueNumber() {
        return itemUniqueNumber;
    }

    public void setItemUniqueNumber(String itemUniqueNumber) {
        this.itemUniqueNumber = itemUniqueNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddOrderDTO that = (AddOrderDTO) o;
        return amount == that.amount &&
                Objects.equals(itemUniqueNumber, that.itemUniqueNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, itemUniqueNumber);
    }

}
