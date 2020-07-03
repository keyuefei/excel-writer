package org.keyuefei.write.converters.string;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;

public class StringStringConverter implements Converter<String> {
    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }


    @Override
    public CellData convertToExcelData(String value) {
        return new CellData(value);
    }

}
