package com.gmail.yauhenizhukovich.app.service.util.reading;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.gmail.yauhenizhukovich.app.service.model.item.AddItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static com.gmail.yauhenizhukovich.app.service.constant.FileConstant.UPLOADED_FOLDER;

public class XmlReadingUtil {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public static List<AddItemDTO> readItems(String originalFilename) throws ParserConfigurationException, SAXException, IOException {
        List<AddItemDTO> items = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        DefaultHandler handler = new ItemDefaultHandler(items);
        saxParser.parse(UPLOADED_FOLDER + originalFilename, handler);
        logger.debug(originalFilename + " successfully parsed.");
        return items;
    }

}

