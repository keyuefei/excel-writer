package org.keyuefei.annotation.write.style;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HeadRowHeight {
    /**
     * Set the header height
     * <p>
     * -1 mean the auto set height
     *
     * @return Header height
     */
    short value() default -1;
}
