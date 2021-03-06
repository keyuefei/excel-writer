package org.keyuefei.write.converters.date;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.util.DateUtils;

import java.util.Date;

public class DateStringConverter implements Converter<Date> {
    @Override
    public Class supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellData convertToExcelData(Date value, ExcelContentProperty excelContentProperty) {
        if (excelContentProperty == null) {
            return new CellData(DateUtils.format(value, null));
        } else {
            return new CellData(DateUtils.format(value, excelContentProperty.getDateTimeFormatProperty().getFormat()));
        }
    }
}
