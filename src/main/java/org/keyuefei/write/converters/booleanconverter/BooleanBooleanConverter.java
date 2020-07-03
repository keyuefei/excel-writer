package org.keyuefei.write.converters.booleanconverter;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

public class BooleanBooleanConverter implements Converter<Boolean> {

    @Override
    public Class supportJavaTypeKey() {
        return Boolean.class;
    }


    @Override
    public CellData convertToExcelData(Boolean value) {
        return new CellData(value);
    }

}
