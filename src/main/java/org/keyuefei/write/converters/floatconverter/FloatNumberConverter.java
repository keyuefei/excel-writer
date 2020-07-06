package org.keyuefei.write.converters.floatconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.NumberUtils;

import java.math.BigDecimal;

public class FloatNumberConverter implements Converter<Float> {

    @Override
    public Class supportJavaTypeKey() {
        return Float.class;
    }


    @Override
    public CellData convertToExcelData(Float value, ExcelContentProperty excelContentProperty) {
        return NumberUtils.formatToCellData(value, excelContentProperty);
    }

}
