package org.keyuefei.model;

import lombok.Data;
import org.keyuefei.condition.ColumnCondition;
import org.keyuefei.matcher.KeyMatcher;

import java.util.List;
import java.util.Map;

@Data
public class Excel {
    private List<ExcelHead> excelHeads;
    private Map<Integer, List<ExcelHead>> level2ExcelHeadsMap;
    private String sheetName;
    //水平表头总行数
    private int horizontalHeadTotalRows;
    //水平表头总列数
    private int horizontalHeadTotalColumns;
    //垂直表头总行数
    private int verticalHeadTotalRows;
    //垂直表头总列数
    private int verticalHeadTotalColumns;
    //总行数
    private int totalRows;
    //总列数
    private int totalColumns;

    //行偏移
    private int rowOffset;
    //列偏移
    private int columnOffset;

    private List<ColumnCondition> columnConditions;
    private KeyMatcher keyMatcher;

    private int[][] content;


    public Excel(List<ExcelHead> excelHeads, Map<Integer, List<ExcelHead>> level2ExcelHeadsMap, String sheetName,
                 int horizontalHeadTotalRows, int horizontalHeadTotalColumns,
                 int verticalHeadTotalRows, int verticalHeadTotalColumns,
                 int totalRows, int totalColumns,
                 int rowOffset, int columnOffset,
                 List<ColumnCondition> columnConditions, KeyMatcher keyMatcher) {
        this.excelHeads = excelHeads;
        this.level2ExcelHeadsMap = level2ExcelHeadsMap;
        this.sheetName = sheetName;
        this.horizontalHeadTotalRows = horizontalHeadTotalRows;
        this.horizontalHeadTotalColumns = horizontalHeadTotalColumns;
        this.verticalHeadTotalRows = verticalHeadTotalRows;
        this.verticalHeadTotalColumns = verticalHeadTotalColumns;
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
        this.columnConditions = columnConditions;
        this.keyMatcher = keyMatcher;
    }
}