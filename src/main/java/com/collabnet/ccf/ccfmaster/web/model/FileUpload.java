package com.collabnet.ccf.ccfmaster.web.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {

    MultipartFile file;
    private String       fieldmappingName = "";

    public String getFieldmappingName() {
        return fieldmappingName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFieldmappingName(String fieldmappingName) {
        this.fieldmappingName = fieldmappingName;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
