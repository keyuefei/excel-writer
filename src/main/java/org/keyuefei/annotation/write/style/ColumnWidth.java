package org.keyuefei.annotation.write.style;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ColumnWidth {

    /**
     * Column width
     * <p>
     * -1 means the default column width is used
     *
     * @return Column width
     */
    int value() default -1;
}
