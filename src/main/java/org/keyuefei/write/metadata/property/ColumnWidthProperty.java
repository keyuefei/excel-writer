package org.keyuefei.write.metadata.property;


import org.keyuefei.annotation.write.style.ColumnWidth;

public class ColumnWidthProperty {
    private Integer width;

    public ColumnWidthProperty(Integer width) {
        this.width = width;
    }

    public static ColumnWidthProperty build(ColumnWidth columnWidth) {
        if (columnWidth == null || columnWidth.value() < 0) {
            return null;
        }
        return new ColumnWidthProperty(columnWidth.value());
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
