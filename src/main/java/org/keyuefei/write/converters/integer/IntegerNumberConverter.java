package org.keyuefei.write.converters.integer;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

import java.math.BigDecimal;

public class IntegerNumberConverter implements Converter<Integer> {

    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }


    @Override
    public CellData convertToExcelData(Integer value) {
        return new CellData(new BigDecimal(Integer.toString(value)));
    }

}
