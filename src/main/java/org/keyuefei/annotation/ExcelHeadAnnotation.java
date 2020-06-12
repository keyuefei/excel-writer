package org.keyuefei.annotation;

import java.lang.annotation.*;


/**
 * @Description 表头注解
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHeadAnnotation {

    /**
     * 内容
     */
    String text() default "";

    /**
     * 描述
     */
    String desc() default "";




    // 表头cell索引
    int index() default 0;

    // 表头cell所在row索引
    int level() default 0;

    // 表头cell的上级cell索引
    int parentIndex() default -1;

    /*类注释属性*/
    // sheet名称
    String sheetName() default "";

    // sheet页中日期类值显示所使用的格式
    String datePattern() default "yyyy-MM-dd";

    // 是否添加序号列
    boolean counted() default false;

    // 是否添加首行
    boolean isHasHeader() default false;
}