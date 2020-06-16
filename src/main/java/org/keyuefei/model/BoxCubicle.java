package org.keyuefei.model;


import org.keyuefei.annotation.ExcelAnnotation;
import org.keyuefei.annotation.ExcelHeadAnnotation;
import org.keyuefei.annotation.ExcelHeadKey;
import org.keyuefei.annotation.ExcelHeadKeys;
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


    @ExcelHeadAnnotation(text = "智能柜I", index = 0, parentIndex = -1)
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
    @ExcelHeadAnnotation(index = 7, parentIndex = 3, text = "无花纹", desc = "智能柜I室内主柜无花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressMainBox")
    @ExcelHeadKey(key = "roomRelation", value = "1")
    @ExcelHeadKey(key = "figureCode", value = "0")
    private String expressBoxInDoorMainNoFigure;


    @ExcelHeadAnnotation(index = 8, parentIndex = 4, text = "logo花纹", desc = "智能柜I室内副柜logo花纹")
    @ExcelHeadKeys(value = {
            @ExcelHeadKey(key = "cubicleType", value = "expressSubBox"),
            @ExcelHeadKey(key = "roomRelation", value = "1"),
            @ExcelHeadKey(key = "figureCode", value = "3"),
    })
    private String expressBoxInDoorSubLogoFigure;

    @ExcelHeadAnnotation(index = 9, parentIndex = 4, text = "左上角印花纹", desc = "智能柜I室内副柜左上角印花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "1")
    @ExcelHeadKey(key = "figureCode", value = "1")
    private String expressBoxInDoorSubLeftFigure;

    @ExcelHeadAnnotation(index = 10, parentIndex = 4, text = "右下角印花纹", desc = "智能柜I室内副柜右下角印花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "1")
    @ExcelHeadKey(key = "figureCode", value = "2")
    private String expressBoxInDoorSubRightFigure;

    @ExcelHeadAnnotation(index = 11, parentIndex = 5, text = "无花纹", desc = "智能柜I室外主柜无花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "2")
    @ExcelHeadKey(key = "figureCode", value = "0")
    private String expressBoxOutDoorMainNoFigure;


    @ExcelHeadAnnotation(index = 12, parentIndex = 6, text = "logo花纹", desc = "智能柜I室外副柜logo花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "2")
    @ExcelHeadKey(key = "figureCode", value = "3")
    private String expressBoxOutDoorSubLogoFigure;

    @ExcelHeadAnnotation(index = 13, parentIndex = 6, text = "左上角印花纹", desc = "智能柜I室外副柜左上角印花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "2")
    @ExcelHeadKey(key = "figureCode", value = "1")
    private String expressBoxOutDoorSubLeftFigure;


    @ExcelHeadAnnotation(index = 14, parentIndex = 6, text = "右下角印花纹", desc = "智能柜I室外副柜右下角印花纹")
    @ExcelHeadKey(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKey(key = "roomRelation", value = "2")
    @ExcelHeadKey(key = "figureCode", value = "2")
    private String expressBoxOutDoorSubRightFigure;

}