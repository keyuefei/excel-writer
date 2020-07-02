package org.keyuefei.write.metadata;


import org.keyuefei.write.accumulator.Accumulator;

public class WriteSheet extends WriteBasicParameter {

    private Integer sheetNo;

    private String sheetName;

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

}
