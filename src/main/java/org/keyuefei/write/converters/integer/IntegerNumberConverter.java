package org.keyuefei.write.converters.integer;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.NumberUtils;

import java.math.BigDecimal;

public class IntegerNumberConverter implements Converter<Integer> {

    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }


    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty excelContentProperty) {
        return NumberUtils.formatToCellData(value, excelContentProperty);
    }

}
