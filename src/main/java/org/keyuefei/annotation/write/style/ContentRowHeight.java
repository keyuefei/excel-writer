package org.keyuefei.annotation.write.style;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ContentRowHeight {

    /**
     * Set the content height
     * <p>
     * -1 mean the auto set height
     *
     * @return Content height
     */
    short value() default -1;
}
