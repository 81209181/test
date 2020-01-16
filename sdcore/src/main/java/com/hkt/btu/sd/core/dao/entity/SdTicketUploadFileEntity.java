package com.hkt.btu.sd.core.dao.entity;

public class SdTicketUploadFileEntity {
    private String fileName;
    private byte[] file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
