package org.keyuefei.write.converters.doubleconverter;

import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.NumberUtils;

import java.math.BigDecimal;

public class DoubleNumberConverter implements Converter<Double> {

    @Override
    public Class supportJavaTypeKey() {
        return Double.class;
    }


    @Override
    public CellData convertToExcelData(Double value, ExcelContentProperty excelContentProperty) {
        return NumberUtils.formatToCellData(value, excelContentProperty);
    }

}
