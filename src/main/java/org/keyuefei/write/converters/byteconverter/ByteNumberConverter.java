package org.keyuefei.write.converters.byteconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;

/**
 * Byte and number converter
 *
 * @author Jiaju Zhuang
 */
public class ByteNumberConverter implements Converter<Byte> {

    @Override
    public Class supportJavaTypeKey() {
        return Byte.class;
    }


    @Override
    public CellData convertToExcelData(Byte value, ExcelContentProperty excelContentProperty) {
        return new CellData(new BigDecimal(Byte.toString(value)));
    }

}
