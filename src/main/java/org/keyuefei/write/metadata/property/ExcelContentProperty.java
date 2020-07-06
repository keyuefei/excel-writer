package org.keyuefei.write.metadata.property;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.ExcelHead;

import java.lang.reflect.Field;

public class ExcelContentProperty {
    /**
     * Java filed
     */
    private Field field;
    /**
     * Excel head
     */
    private ExcelHead head;
    /**
     * Custom defined converters
     */
    private Converter converter;
    private DateTimeFormatProperty dateTimeFormatProperty;
    private NumberFormatProperty numberFormatProperty;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ExcelHead getHead() {
        return head;
    }

    public void setHead(ExcelHead head) {
        this.head = head;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public DateTimeFormatProperty getDateTimeFormatProperty() {
        return dateTimeFormatProperty;
    }

    public void setDateTimeFormatProperty(DateTimeFormatProperty dateTimeFormatProperty) {
        this.dateTimeFormatProperty = dateTimeFormatProperty;
    }

    public NumberFormatProperty getNumberFormatProperty() {
        return numberFormatProperty;
    }

    public void setNumberFormatProperty(NumberFormatProperty numberFormatProperty) {
        this.numberFormatProperty = numberFormatProperty;
    }
}
