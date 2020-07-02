package org.keyuefei.write;

import org.keyuefei.write.builder.ExcelWriterBuilder;

/**
 * @Description
 * @Author 003654
 * @Date 2020/6/29
 * @Time 9:56
 */
public class DynamicExcel {

    public static ExcelWriterBuilder write(String pathName, Class head) {
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file(pathName);
        if (head != null) {
            excelWriterBuilder.head(head);
        }
        return excelWriterBuilder;
    }

}