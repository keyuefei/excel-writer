package org.keyuefei.write.converters.bigdecimal;

import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.NumberUtils;

import java.math.BigDecimal;

public class BigDecimalNumberConverter implements Converter<BigDecimal> {

    @Override
    public Class supportJavaTypeKey() {
        return BigDecimal.class;
    }



    @Override
    public CellData convertToExcelData(BigDecimal value, ExcelContentProperty excelContentProperty) {
        return NumberUtils.formatToCellData(value, excelContentProperty);
    }
}
