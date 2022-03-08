package com.company.elixr.springbootapplicationwithdocker.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String STATUS = "success";
    public static final String SUCCESS = "true";
    public static final String FAILURE = "false";
    public static final String REQUIRED_FILE_TYPE = "text/plain";
    public static final String LOCAL_STORAGE_FOLDER_PATH = "${file.upload-dir}";
    public static final String FILE_PRESENT_IN_THE_SYSTEM = "Present";
    public static final String FILE_NOT_PRESENT_IN_THE_SYSTEM = "Absent";
    public static final String ERROR_CREATING_UPLOAD_DIRECTORY = "Could not create upload directory";
    public static final String ERROR_INVALID_URL_PATH = "Invalid URL path";
    public static final String ERROR_UPLOADING_FILE = "Could not upload file";
    public static final String ERROR_NOT_FOUND = "No record found";
    public static final String ERROR_BAD_REQUEST_FILE_NOT_PRESENT_OR_INVALID_FILE_TYPE = "No file is chosen in the " +
            "multipart form or chosen file type is invalid";
    public static final String ERROR_UNEXPECTED_TYPE = "Unexpected internal server error";
    public static final String ERROR_BAD_REQUEST_INVALID_ID_FORMAT = "id is not in UUID format";
    public static final String ERROR_BAD_REQUEST_SERVLET_REQUEST_PART_FILE_MISSING = "Servlet request part 'file' " +
            "missing";
    public static final String ERROR_BAD_REQUEST_SERVLET_REQUEST_PART_USERNAME_MISSING = "Servlet request part " +
            "'username' missing";
    public static final String ERROR_BAD_REQUEST_REQUEST_PARAM_USERNAME_MISSING = "Username is a mandatory field";
}
