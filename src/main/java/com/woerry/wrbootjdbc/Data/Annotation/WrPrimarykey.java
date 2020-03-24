package com.woerry.wrbootjdbc.Data.Annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrPrimarykey {
    String name() ;
    int size() default 0;
    String type() default "";
    String remark() default "";
    int sequence() default 0;
}
