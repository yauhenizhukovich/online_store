package com.gmail.yauhenizhukovich.app.service.util.reading;

import java.math.BigDecimal;
import java.util.List;

import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ItemDefaultHandler extends DefaultHandler {

    private boolean isName = false;
    private boolean isPrice = false;
    private boolean isDescription = false;
    private AddItemDTO item = new AddItemDTO();
    private List<AddItemDTO> items;

    public ItemDefaultHandler(List<AddItemDTO> items) {
        this.items = items;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("name")) {
            isName = true;
        }
        if (qName.equalsIgnoreCase("price")) {
            isPrice = true;
        }
        if (qName.equalsIgnoreCase("description")) {
            isDescription = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (item.getName() != null && item.getDescription() != null && item.getPrice() != null) {
            items.add(item);
            item = new AddItemDTO();
        }
        if (isName) {
            String name = new String(ch, start, length);
            item.setName(name);
            isName = false;
        }
        if (isPrice) {
            String stringPrice = new String(ch, start, length);
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(stringPrice));
            item.setPrice(price);
            isPrice = false;
        }
        if (isDescription) {
            String description = new String(ch, start, length);
            item.setDescription(description);
            isDescription = false;
        }
    }

}

