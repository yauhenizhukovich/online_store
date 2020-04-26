package com.gmail.yauhenizhukovich.app.repository.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "item_details")
public class ItemDetails {

    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "item"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "item_id", unique = true, nullable = false)
    private Long itemId;
    @Column
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Item item;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDetails that = (ItemDetails) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, description);
    }

}
