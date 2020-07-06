package org.keyuefei.demo2.converts;

import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

/**
 * @Description 性别转换器
 * @Author 003654
 * @Date 2020/7/6
 * @Time 18:03
 */
public class GenderConvert implements Converter {

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellData convertToExcelData(Object value, ExcelContentProperty excelContentProperty) throws Exception {
        if (1 == Integer.valueOf(value.toString())) {
            return new CellData("男");
        }
        return new CellData("女");
    }
}