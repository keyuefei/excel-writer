package org.keyuefei.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.keyuefei.data.TestData1;

import java.util.List;

@Data
@NoArgsConstructor
public class ExcelHead {
    private String text;
    private int index;
    private int parentIndex;

    private int row;
    private int col;

    //合并列
    private int colSpan;
    //合并行
    private int rowSpan;

    //父表头
    private ExcelHead parentHead;
    //子表头
    private List<ExcelHead> childHeads;

    //横向分组 数据； 横向表头有值
    private List<TestData1> rowGroupData;

    /**
     * 水平叶子节点
     */
    private boolean isHorizontalLeaf;
    /**
     * key， 水平叶子节点才有该属性
     */
    private List<ExcelHeadKey> headKeys;

    private ExcelHeadGroup excelHeadGroup;


    public ExcelHead(String text, int index, int parentIndex, int colSpan, int rowSpan,
                     ExcelHead parentHead, List<ExcelHead> childHeads, List<TestData1> rowGroupData,
                     boolean isHorizontalLeaf, List<ExcelHeadKey> headKeys) {
        this.text = text;
        this.index = index;
        this.parentIndex = parentIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.parentHead = parentHead;
        this.childHeads = childHeads;
        this.rowGroupData = rowGroupData;
        this.isHorizontalLeaf = isHorizontalLeaf;
        this.headKeys = headKeys;
    }

    @Override
    public String toString() {
        return "ExcelHead{" +
                "text='" + text + '\'' +
                ", index=" + index +
                ", parentIndex=" + parentIndex +
                ", row=" + row +
                ", col=" + col +
                ", colSpan=" + colSpan +
                ", rowSpan=" + rowSpan +
                ", parentHead=" + parentHead +
                ", rowGroupData=" + rowGroupData +
                ", isHorizontalLeaf=" + isHorizontalLeaf +
                ", headKeys=" + headKeys +
                ", excelHeadGroup=" + excelHeadGroup +
                '}';
    }
}