package org.keyuefei.demo1.annotation;


import org.keyuefei.annotation.ExcelGroup;
import org.keyuefei.annotation.ExcelMatch;
import org.keyuefei.annotation.ExcelProperty;

import java.math.BigDecimal;


/**
 * @Description 测试
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
public class StudentScore {

    @ExcelGroup(key = "grade")
    @ExcelProperty(value = "年级")
    private String grade;

    @ExcelGroup(key = "className")
    @ExcelProperty(value = "班级")
    private String className;

    @ExcelGroup(key = "studentName")
    @ExcelProperty(value = {"学生"})
    private String studentName;

    @ExcelMatch(key = "year", value = "2018")
    @ExcelMatch(key = "course", value = "chinese")
    @ExcelMatch(key = "semester", value = "last")
    @ExcelProperty(value = {"2018年", "上学期", "语文"})
    private BigDecimal year2018lastChinese;

    @ExcelMatch(key = "year", value = "2018")
    @ExcelMatch(key = "semester", value = "last")
    @ExcelMatch(key = "course", value = "english")
    @ExcelProperty(value = {"2018年", "上学期", "英语"})
    private BigDecimal year2018lastEnglish;


    @ExcelMatch(key = "year", value = "2018")
    @ExcelMatch(key = "semester", value = "next")
    @ExcelMatch(key = "course", value = "chinese")
    @ExcelProperty(value = {"2018年", "下学期", "语文"})
    private BigDecimal year2018nextChinese;

    @ExcelMatch(key = "year", value = "2018")
    @ExcelMatch(key = "semester", value = "next")
    @ExcelMatch(key = "course", value = "english")
    @ExcelProperty(value = {"2018年", "下学期", "英语"})
    private BigDecimal year2018nextEnglish;

    @ExcelMatch(key = "year", value = "2019")
    @ExcelMatch(key = "semester", value = "last")
    @ExcelMatch(key = "course", value = "chinese")
    @ExcelProperty(value = {"2019年", "上学期", "语文"})
    private BigDecimal year2019lastChinese;

    @ExcelMatch(key = "year", value = "2019")
    @ExcelMatch(key = "semester", value = "last")
    @ExcelMatch(key = "course", value = "english")
    @ExcelProperty(value = {"2019年", "上学期", "英语"})
    private BigDecimal year2019lastEnglish;

    @ExcelMatch(key = "year", value = "2019")
    @ExcelMatch(key = "semester", value = "next")
    @ExcelMatch(key = "course", value = "chinese")
    @ExcelProperty(value = {"2019年", "下学期", "语文"})
    private BigDecimal year2019nextChinese;

    @ExcelMatch(key = "year", value = "2019")
    @ExcelMatch(key = "semester", value = "next")
    @ExcelMatch(key = "course", value = "english")
    @ExcelProperty(value = {"2019年", "下学期", "英语"})
    private BigDecimal year2019nextEnglish;


}