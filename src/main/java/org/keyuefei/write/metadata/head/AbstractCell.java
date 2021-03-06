package org.keyuefei.write.metadata.head;

public class AbstractCell implements Cell {
    /**
     * Row index
     */
    private Integer rowIndex;
    /**
     * Column index
     */
    private Integer columnIndex;

    @Override
    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }
}
