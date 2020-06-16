package org.keyuefei.annotation;

import java.lang.annotation.*;

/**
 * @author ExcelHeadKey 1063847690@qq.com
 * @Classname ExcelHeadKey
 * @Description TODO
 * @Date 2020/6/15 16:51
 * @Created by keyuefei
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = ExcelHeadKeysAnnotation.class)
public @interface ExcelHeadKeyAnnotation {

    String key() default "";

    String value() default "";

}
