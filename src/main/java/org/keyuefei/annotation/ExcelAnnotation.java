package org.keyuefei.annotation;

import org.keyuefei.condition.ColumnCondition;
import org.keyuefei.matcher.KeyMatcher;

import javax.xml.ws.Action;
import java.lang.annotation.*;

/**
 * @Description 表头注解
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
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
    /**
     * 键匹配器
     */
    Class<? extends KeyMatcher> keMatcher();

    /**
     * 行起始偏移
     */
    int rowOffset() default 0;

    /**
     * 列起始偏移
     */
    int columnOffset() default 0;


}
