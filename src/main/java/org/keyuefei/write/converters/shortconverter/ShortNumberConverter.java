package org.keyuefei.write.converters.shortconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

import java.math.BigDecimal;

/**
 * Short and number converter
 *
 * @author Jiaju Zhuang
 */
public class ShortNumberConverter implements Converter<Short> {

    @Override
    public Class supportJavaTypeKey() {
        return Short.class;
    }


    @Override
    public CellData convertToExcelData(Short value) {
        return new CellData(new BigDecimal(Short.toString(value)));
    }

}
