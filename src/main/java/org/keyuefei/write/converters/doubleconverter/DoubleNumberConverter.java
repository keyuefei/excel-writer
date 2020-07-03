package org.keyuefei.write.converters.doubleconverter;

import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

import java.math.BigDecimal;

public class DoubleNumberConverter implements Converter<Double> {

    @Override
    public Class supportJavaTypeKey() {
        return Double.class;
    }


    @Override
    public CellData convertToExcelData(Double value) {
        return new CellData(BigDecimal.valueOf(value));
    }

}
