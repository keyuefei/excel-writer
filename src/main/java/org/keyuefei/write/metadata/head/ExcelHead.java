package org.keyuefei.write.metadata.head;

import org.keyuefei.write.metadata.property.ColumnWidthProperty;
import org.keyuefei.write.metadata.property.FontProperty;
import org.keyuefei.write.metadata.property.StyleProperty;

import java.lang.reflect.Field;

public class ExcelHead {
    /**
     * 文本
     */
    private String text;
    /**
     * 行
     */
    private int row;
    /**
     * 列
     */
    private int col;
    /**
     *合并行
     */
    private int rowSpan;
    /**
     *合并列
     */
    private int colSpan;
    /**
     *父表头
     */
    private ExcelHead parentHead;

    /**
     * 对象属性
     */
    private Field field;

    /**
     * column with
     */
    private ColumnWidthProperty columnWidthProperty;
    /**
     * Head style
     */
    private StyleProperty headStyleProperty;
    /**
     * Content style
     */
    private StyleProperty contentStyleProperty;
    /**
     * Head font
     */
    private FontProperty headFontProperty;
    /**
     * Content font
     */
    private FontProperty contentFontProperty;


    public ExcelHead(String text, int row, int col, int rowSpan, int colSpan, ExcelHead parentHead) {
        this.text = text;
        this.row = row;
        this.col = col;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        this.parentHead = parentHead;
    }

    public ExcelHead() {
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public ExcelHead getParentHead() {
        return parentHead;
    }

    public void setParentHead(ExcelHead parentHead) {
        this.parentHead = parentHead;
    }

    public ColumnWidthProperty getColumnWidthProperty() {
        return columnWidthProperty;
    }

    public void setColumnWidthProperty(ColumnWidthProperty columnWidthProperty) {
        this.columnWidthProperty = columnWidthProperty;
    }


    public StyleProperty getHeadStyleProperty() {
        return headStyleProperty;
    }

    public void setHeadStyleProperty(StyleProperty headStyleProperty) {
        this.headStyleProperty = headStyleProperty;
    }

    public StyleProperty getContentStyleProperty() {
        return contentStyleProperty;
    }

    public void setContentStyleProperty(StyleProperty contentStyleProperty) {
        this.contentStyleProperty = contentStyleProperty;
    }

    public FontProperty getHeadFontProperty() {
        return headFontProperty;
    }

    public void setHeadFontProperty(FontProperty headFontProperty) {
        this.headFontProperty = headFontProperty;
    }

    public FontProperty getContentFontProperty() {
        return contentFontProperty;
    }

    public void setContentFontProperty(FontProperty contentFontProperty) {
        this.contentFontProperty = contentFontProperty;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}