package org.keyuefei.write.metadata.holder;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.keyuefei.write.metadata.WriteSheet;
import org.keyuefei.write.metadata.property.ExcelWriteHeadProperty;


public class WriteSheetHolder extends AbstractWriteHolder {
    private WriteSheet writeSheet;

    private Sheet sheet;


    private Integer sheetNo;

    private String sheetName;

    private WriteWorkbookHolder parentWriteWorkbookHolder;

    /**
     * Excel head property
     */
    private ExcelWriteHeadProperty excelWriteHeadProperty;

    public WriteSheetHolder(WriteSheet writeSheet, WriteWorkbookHolder writeWorkbookHolder) {
        super(writeSheet, writeWorkbookHolder);
        this.writeSheet = writeSheet;
        if (writeSheet.getSheetNo() == null && StringUtils.isEmpty(writeSheet.getSheetName())) {
            this.sheetNo = 0;
        } else {
            this.sheetNo = writeSheet.getSheetNo();
        }
        this.sheetName = writeSheet.getSheetName();
        this.parentWriteWorkbookHolder = writeWorkbookHolder;

        // Initialization property
        this.excelWriteHeadProperty = new ExcelWriteHeadProperty(this, getClazz());
    }



    public WriteSheet getWriteSheet() {
        return writeSheet;
    }

    public void setWriteSheet(WriteSheet writeSheet) {
        this.writeSheet = writeSheet;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public WriteWorkbookHolder getParentWriteWorkbookHolder() {
        return parentWriteWorkbookHolder;
    }

    public void setParentWriteWorkbookHolder(WriteWorkbookHolder parentWriteWorkbookHolder) {
        this.parentWriteWorkbookHolder = parentWriteWorkbookHolder;
    }

    public ExcelWriteHeadProperty getExcelWriteHeadProperty() {
        return excelWriteHeadProperty;
    }

    public void setExcelWriteHeadProperty(ExcelWriteHeadProperty excelWriteHeadProperty) {
        this.excelWriteHeadProperty = excelWriteHeadProperty;
    }


}
