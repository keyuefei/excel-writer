package org.keyuefei.annotation;

import org.keyuefei.condition.ColumnCondition;

import javax.xml.ws.Action;

/**
 * @Description 表头注解
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
public @interface ExcelAnnotation {

    String sheetName() default "sheet1";

    /**
     * 是否删除无效列。
     * @return
     */
    boolean delInvalidColumn() default true;
    /**
     * 无效列的条件。
     */
    Class<? extends ColumnCondition>[] invalidColumnCondition();


}
