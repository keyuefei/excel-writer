package org.keyuefei.write.metadata;


import lombok.Data;
import org.keyuefei.support.ExcelTypeEnum;

import java.io.File;
import java.io.OutputStream;

public class WriteWorkbook extends WriteBasicParameter {
    private File file;

    private OutputStream outputStream;

    private Boolean autoCloseStream;

    private ExcelTypeEnum excelType;

    private Boolean inMemory;

    private Boolean writeExcelOnException;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Boolean getAutoCloseStream() {
        return autoCloseStream;
    }

    public void setAutoCloseStream(Boolean autoCloseStream) {
        this.autoCloseStream = autoCloseStream;
    }

    public ExcelTypeEnum getExcelType() {
        return excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public Boolean getWriteExcelOnException() {
        return writeExcelOnException;
    }

    public void setWriteExcelOnException(Boolean writeExcelOnException) {
        this.writeExcelOnException = writeExcelOnException;
    }

    public Boolean getInMemory() {
        return inMemory;
    }

    public void setInMemory(Boolean inMemory) {
        this.inMemory = inMemory;
    }
}
