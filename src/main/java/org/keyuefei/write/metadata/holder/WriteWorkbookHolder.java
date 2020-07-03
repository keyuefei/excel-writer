package org.keyuefei.write.metadata.holder;

import org.apache.poi.ss.usermodel.Workbook;
import org.keyuefei.enums.ExcelTypeEnum;
import org.keyuefei.exception.ExcelGenerateException;
import org.keyuefei.write.metadata.WriteWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class WriteWorkbookHolder extends AbstractWriteHolder {

    private Workbook workbook;

    private WriteWorkbook writeWorkbook;

    private File file;

    private OutputStream outputStream;

    private Boolean autoCloseStream;

    private ExcelTypeEnum excelType;

    private Boolean inMemory;

    private Boolean writeExcelOnException;

    public WriteWorkbookHolder(WriteWorkbook writeWorkbook) {
        super(writeWorkbook, null);
        this.writeWorkbook = writeWorkbook;
        this.file = writeWorkbook.getFile();
        if (file != null) {
            try {
                this.outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new ExcelGenerateException("Can not found file.", e);
            }
        } else {
            this.outputStream = writeWorkbook.getOutputStream();
        }
        if (writeWorkbook.getAutoCloseStream() == null) {
            this.autoCloseStream = Boolean.TRUE;
        } else {
            this.autoCloseStream = writeWorkbook.getAutoCloseStream();
        }

        if (writeWorkbook.getExcelType() == null) {
            boolean isXls = (file != null && file.getName().endsWith(ExcelTypeEnum.XLS.getValue()));
            if (isXls) {
                this.excelType = ExcelTypeEnum.XLS;
            } else {
                this.excelType = ExcelTypeEnum.XLSX;
            }
        } else {
            this.excelType = writeWorkbook.getExcelType();
        }

        if (writeWorkbook.getInMemory() == null) {
            this.inMemory = Boolean.FALSE;
        } else {
            this.inMemory = writeWorkbook.getInMemory();
        }

        if (writeWorkbook.getWriteExcelOnException() == null) {
            this.writeExcelOnException = Boolean.FALSE;
        } else {
            this.writeExcelOnException = writeWorkbook.getWriteExcelOnException();
        }
    }


    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public WriteWorkbook getWriteWorkbook() {
        return writeWorkbook;
    }

    public void setWriteWorkbook(WriteWorkbook writeWorkbook) {
        this.writeWorkbook = writeWorkbook;
    }

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
