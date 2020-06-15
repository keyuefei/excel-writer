package org.keyuefei.model;


import org.keyuefei.annotation.ExcelAnnotation;
import org.keyuefei.annotation.ExcelHeadAnnotation;
import org.keyuefei.annotation.ExcelLeafHeadAnnotation;
import org.keyuefei.condition.ColumnSumZeroCondition;
import org.keyuefei.matcher.DefaultKeyMatcher;


/**
 * @Description 测试
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
@ExcelAnnotation(sheetName = "测试", delInvalidColumn = true,
        invalidColumnCondition = {ColumnSumZeroCondition.class}, keMatcher = DefaultKeyMatcher.class)
public class BoxCubicle {


    @ExcelHeadAnnotation(text = "智能柜I", index = -1, parentIndex = -1)
    private String expressBox;

    @ExcelHeadAnnotation(text = "室内", desc = "智能柜I室内",
            index = 1, parentIndex = 0)
    private String expressBoxInDoor;

    @ExcelHeadAnnotation(text = "室外", desc = "智能柜I室外",
            index = 2, parentIndex = 0)
    private String expressBoxOutDoor;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室内主柜",
            index = 3, parentIndex = 1)
    private String expressBoxInDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室内副柜",
            index = 4, parentIndex = 1)
    private String expressBoxInDoorSub;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室外主柜",
            index = 5, parentIndex = 2)
    private String expressBoxOutDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室外副柜",
            index = 6, parentIndex = 2)
    private String expressBoxOutDoorSub;


    /**
     * 叶子表头必须含有属性 key
     */
    @ExcelLeafHeadAnnotation(index = 7, parentIndex = 3, key = "expressBoxInDoorMainNoFigure", text = "无花纹", desc = "智能柜I室内主柜无花纹")
    private String expressBoxInDoorMainNoFigure;

    @ExcelLeafHeadAnnotation(index = 8, parentIndex = 4, key = "expressBoxInDoorSubLogoFigure", text = "logo花纹", desc = "智能柜I室内副柜logo花纹")
    private String expressBoxInDoorSubLogoFigure;

    @ExcelLeafHeadAnnotation(index = 9, parentIndex = 4, key = "expressBoxInDoorSubLeftFigure", text = "左上角印花纹", desc = "智能柜I室内副柜左上角印花纹")
    private String expressBoxInDoorSubLeftFigure;

    @ExcelLeafHeadAnnotation(index = 10, parentIndex = 4, key = "expressBoxInDoorSubRightFigure", text = "右下角印花纹", desc = "智能柜I室内副柜右下角印花纹")
    private String expressBoxInDoorSubRightFigure;

    @ExcelLeafHeadAnnotation(index = 11, parentIndex = 5, key = "expressBoxOutDoorMainNoFigure", text = "无花纹", desc = "智能柜I室外主柜无花纹")
    private String expressBoxOutDoorMainNoFigure;

    @ExcelLeafHeadAnnotation(index = 12, parentIndex = 6, key = "expressBoxOutDoorSubLogoFigure", text = "logo花纹", desc = "智能柜I室外副柜logo花纹")
    private String expressBoxOutDoorSubLogoFigure;

    @ExcelLeafHeadAnnotation(index = 13, parentIndex = 6, key = "expressBoxOutDoorSubLeftFigure", text = "左上角印花纹", desc = "智能柜I室外副柜左上角印花纹")
    private String expressBoxOutDoorSubLeftFigure;

    @ExcelLeafHeadAnnotation(index = 14, parentIndex = 6, key = "expressBoxOutDoorSubRightFigure", text = "右下角印花纹", desc = "智能柜I室外副柜右下角印花纹")
    private String expressBoxOutDoorSubRightFigure;


}