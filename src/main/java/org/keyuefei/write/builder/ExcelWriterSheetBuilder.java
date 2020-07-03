package org.keyuefei.write.builder;


import org.keyuefei.exception.ExcelGenerateException;
import org.keyuefei.write.ExcelWriter;
import org.keyuefei.write.accumulator.Accumulator;
import org.keyuefei.write.metadata.WriteSheet;

import java.util.List;

public class ExcelWriterSheetBuilder  {
    private ExcelWriter excelWriter;
    /**
     * Sheet
     */
    private WriteSheet writeSheet;


    public ExcelWriterSheetBuilder(ExcelWriter excelWriter) {
        this.writeSheet = new WriteSheet();
        this.excelWriter = excelWriter;
    }

    /**
     * Starting from 0
     *
     * @param sheetNo
     * @return
     */
    public ExcelWriterSheetBuilder sheetNo(Integer sheetNo) {
        writeSheet.setSheetNo(sheetNo);
        return this;
    }

    /**
     * sheet name
     *
     * @param sheetName
     * @return
     */
    public ExcelWriterSheetBuilder sheetName(String sheetName) {
        writeSheet.setSheetName(sheetName);
        return this;
    }

    public ExcelWriterSheetBuilder accumulator(Accumulator<Object> accumulator) {
        writeSheet.setAccumulator(accumulator);
        return this;
    }


    public WriteSheet build() {
        return writeSheet;
    }

    public void doWrite(List data) {
        if (excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet()' to call this method");
        }
        excelWriter.write(data, build());
        excelWriter.finish();
    }


}
