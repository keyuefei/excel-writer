package org.keyuefei.write.converters.longconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.NumberUtils;

import java.math.BigDecimal;

public class LongNumberConverter implements Converter<Long> {

    @Override
    public Class supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public CellData convertToExcelData(Long value, ExcelContentProperty excelContentProperty) {
        return NumberUtils.formatToCellData(value, excelContentProperty);
    }

}
