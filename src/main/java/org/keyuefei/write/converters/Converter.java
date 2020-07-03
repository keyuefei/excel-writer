package org.keyuefei.write.converters;


import org.keyuefei.write.metadata.head.CellData;

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
     * @return Data to put into a Excel
     * @throws Exception Exception.
     */
    CellData convertToExcelData(T value)
            throws Exception;
}
