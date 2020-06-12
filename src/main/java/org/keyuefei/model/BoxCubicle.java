package org.keyuefei.model;


import org.keyuefei.annotation.ExcelAnnotation;
import org.keyuefei.annotation.ExcelHeadAnnotation;
import org.keyuefei.condition.ColumnSumZeroCondition;

import java.io.Serializable;


/**
 * @Description 测试
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
@ExcelAnnotation(sheetName = "测试", delInvalidColumn = true, invalidColumnCondition = {ColumnSumZeroCondition.class})
public class BoxCubicle implements Serializable {


    @ExcelHeadAnnotation(text = "智能柜I",
            index = 0, level = 0, parentIndex = -1)
    private String expressBox;

    @ExcelHeadAnnotation(text = "室内", desc = "智能柜I室内",
            index = 1, level = 1, parentIndex = 0)
    private String expressBoxInDoor;

    @ExcelHeadAnnotation(text = "室外", desc = "智能柜I室外",
            index = 2, level = 1, parentIndex = 0)
    private String expressBoxOutDoor;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室内主柜",
            index = 3, level = 2, parentIndex = 1)
    private String expressBoxInDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室内副柜",
            index = 4, level = 2, parentIndex = 1)
    private String expressBoxInDoorSub;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室外主柜",
            index = 5, level = 2, parentIndex = 2)
    private String expressBoxOutDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室外副柜",
            index = 6, level = 2, parentIndex = 2)
    private String expressBoxOutDoorSub;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室外主柜无花纹",
            index = 7, level = 3, parentIndex = 3)
    private String expressBoxOutDoorMainNoFigure;







}