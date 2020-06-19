package org.keyuefei.model;

import lombok.Data;
import org.keyuefei.accumulator.Accumulator;
import org.keyuefei.condition.ColumnCondition;
import org.keyuefei.matcher.KeyMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Excel {
    private List<ExcelHead> excelHeads;
    private Map<Integer, List<ExcelHead>> horizontalLevel2ExcelHeadsMap;

    private Map<Integer, List<ExcelHead>> verticalLevel2ExcelHeadsMap;
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
    /**
     * 分组的列数
     */
    private int groupColumns;
    /**
     * 分组
     */
    private List<ExcelHeadGroup> excelHeadGroups;


    private List<ColumnCondition> columnConditions;
    private KeyMatcher keyMatcher;

    private Accumulator accumulator;


    private int[][] content;


    public Excel() {
    }
}