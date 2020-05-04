package com.gmail.yauhenizhukovich.app.service.constant;

import java.io.File;

public interface FileConstant {

    String FILE_UPLOAD_ERROR = "Can not read file. Some problems with content or file type.";
    String SELECT_FILE_MESSAGE = "Please select a file to upload.";
    String UPLOADED_FOLDER = "X:" + File.separator + File.separator +
            "uploaded" + File.separator +
            "xml" + File.separator;

}
