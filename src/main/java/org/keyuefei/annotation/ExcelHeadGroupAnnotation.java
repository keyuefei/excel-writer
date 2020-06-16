package org.keyuefei.annotation;

import java.lang.annotation.*;

/**
 * @author Group 1063847690@qq.com
 * @Classname Group
 * @Description TODO
 * @Date 2020/6/16 15:27
 * @Created by keyuefei
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHeadGroupAnnotation {
    String key();
}
