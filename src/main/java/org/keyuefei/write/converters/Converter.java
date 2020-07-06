package org.keyuefei.write.converters;


import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

public interface Converter<T> {

    /**
     * Back to object types in Java
     *
     * @return Support for Java class
     */
    Class supportJavaTypeKey();


    /**
     * Convert Java objects to excel objects
     *
     * @param value               Java Data.NotNull.
     * @param excelContentProperty
     * @return Data to put into a Excel
     * @throws Exception Exception.
     */
    CellData convertToExcelData(T value, ExcelContentProperty excelContentProperty)
            throws Exception;
}
