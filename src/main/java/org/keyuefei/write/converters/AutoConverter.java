package org.keyuefei.write.converters;


import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

public class AutoConverter implements Converter {

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }


    @Override
    public CellData convertToExcelData(Object value, ExcelContentProperty excelContentProperty) {
        return null;
    }
}
