package org.keyuefei.annotation;



import org.keyuefei.write.converters.AutoConverter;
import org.keyuefei.write.converters.Converter;

import java.lang.annotation.*;

/**
 * @author jipengfei
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    /**
     * The name of the sheet header.
     *
     * <p>
     * write: It automatically merges when you have more than one head
     * <p>
     * read: When you have multiple heads, take the first one
     *
     * @return The name of the sheet header
     */
    String[] value() default {""};

    /**
     * Index of column
     *
     * Read or write it on the index of column,If it's equal to -1, it's sorted by Java class.
     *
     * priority: index &gt; order &gt; default sort
     *
     * @return Index of column
     */
    int index() default -1;

    /**
     * Defines the sort order for an column.
     *
     * priority: index &gt; order &gt; default sort
     *
     * @return Order of column
     */
    int order() default Integer.MAX_VALUE;

    /**
     * Force the current field to use this converter.
     *
     * @return Converter
     */
    Class<? extends Converter> converter() default AutoConverter.class;

}
