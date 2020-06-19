package org.keyuefei.demo1.annotation;


import org.keyuefei.annotation.*;
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

    @ExcelHeadGroupAnnotation(key = "region")
    @ExcelHeadAnnotation(text = "大区", index = 0, parentIndex = -1)
    private String region;

    @ExcelHeadGroupAnnotation( key = "boxCityName")
    @ExcelHeadAnnotation(text = "城市", index = 1, parentIndex = -1)
    private String boxCityName;

    @ExcelHeadGroupAnnotation(key = "supplierName")
    @ExcelHeadAnnotation(text = "供应商", index = 2, parentIndex = -1)
    private String supplierName;


    @ExcelHeadAnnotation(text = "智能柜I", index = 3, parentIndex = -1)
    private String expressBox;

    @ExcelHeadAnnotation(text = "室内", desc = "智能柜I室内",
            index = 4, parentIndex = 3)
    private String expressBoxInDoor;

    @ExcelHeadAnnotation(text = "室外", desc = "智能柜I室外",
            index = 5, parentIndex = 3)
    private String expressBoxOutDoor;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室内主柜",
            index = 6, parentIndex = 4)
    private String expressBoxInDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室内副柜",
            index = 7, parentIndex = 4)
    private String expressBoxInDoorSub;

    @ExcelHeadAnnotation(text = "主柜", desc = "智能柜I室外主柜",
            index = 8, parentIndex = 5)
    private String expressBoxOutDoorMain;

    @ExcelHeadAnnotation(text = "副柜", desc = "智能柜I室外副柜",
            index = 9, parentIndex = 5)
    private String expressBoxOutDoorSub;


    /**
     * 叶子表头必须含有属性 key
     */
    @ExcelHeadAnnotation(index = 10, parentIndex = 6, text = "无花纹", desc = "智能柜I室内主柜无花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressMainBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "1")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "0")
    private String expressBoxInDoorMainNoFigure;


    @ExcelHeadAnnotation(index = 11, parentIndex = 7, text = "logo花纹", desc = "智能柜I室内副柜logo花纹")
    @ExcelHeadKeysAnnotation(value = {
            @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox"),
            @ExcelHeadKeyAnnotation(key = "roomRelation", value = "1"),
            @ExcelHeadKeyAnnotation(key = "figureCode", value = "3"),
    })
    private String expressBoxInDoorSubLogoFigure;

    @ExcelHeadAnnotation(index = 12, parentIndex = 7, text = "左上角印花纹", desc = "智能柜I室内副柜左上角印花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "1")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "1")
    private String expressBoxInDoorSubLeftFigure;

    @ExcelHeadAnnotation(index = 13, parentIndex = 7, text = "右下角印花纹", desc = "智能柜I室内副柜右下角印花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "1")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "2")
    private String expressBoxInDoorSubRightFigure;

    @ExcelHeadAnnotation(index = 14, parentIndex = 8, text = "无花纹", desc = "智能柜I室外主柜无花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressMainBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "2")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "0")
    private String expressBoxOutDoorMainNoFigure;


    @ExcelHeadAnnotation(index = 15, parentIndex = 9, text = "logo花纹", desc = "智能柜I室外副柜logo花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "2")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "3")
    private String expressBoxOutDoorSubLogoFigure;

    @ExcelHeadAnnotation(index = 16, parentIndex = 9, text = "左上角印花纹", desc = "智能柜I室外副柜左上角印花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "2")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "1")
    private String expressBoxOutDoorSubLeftFigure;


    @ExcelHeadAnnotation(index = 17, parentIndex = 9, text = "右下角印花纹", desc = "智能柜I室外副柜右下角印花纹")
    @ExcelHeadKeyAnnotation(key = "cubicleType", value = "expressSubBox")
    @ExcelHeadKeyAnnotation(key = "roomRelation", value = "2")
    @ExcelHeadKeyAnnotation(key = "figureCode", value = "2")
    private String expressBoxOutDoorSubRightFigure;

    public BoxCubicle() {
    }
}