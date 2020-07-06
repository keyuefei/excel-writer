package org.keyuefei.write.converters.booleanconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

public class BooleanBooleanConverter implements Converter<Boolean> {

    @Override
    public Class supportJavaTypeKey() {
        return Boolean.class;
    }


    @Override
    public CellData convertToExcelData(Boolean value, ExcelContentProperty excelContentProperty) {
        return new CellData(value);
    }

}
