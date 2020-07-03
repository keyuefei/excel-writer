package org.keyuefei.write.converters.floatconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

import java.math.BigDecimal;

/**
 * Float and number converter
 *
 * @author Jiaju Zhuang
 */
public class FloatNumberConverter implements Converter<Float> {

    @Override
    public Class supportJavaTypeKey() {
        return Float.class;
    }


    @Override
    public CellData convertToExcelData(Float value) {
        return new CellData(new BigDecimal(Float.toString(value)));
    }

}
