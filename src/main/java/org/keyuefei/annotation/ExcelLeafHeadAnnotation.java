package org.keyuefei.annotation;

import java.lang.annotation.*;


/**
 * @Description 表头注解
 * @Author 003654
 * @Date 2020/6/12
 * @Time 16:59
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelLeafHeadAnnotation {

    /**
     * 唯一标识，索引
     */
    int index();
    /**
     * 父级表头索引， -1 为顶级表头。
     */
    int parentIndex();
    /**
     * 内容
     */
    String text();
    /**
     *
     */
    String key();
    /**
     * 描述
     */
    String desc() default "";



}