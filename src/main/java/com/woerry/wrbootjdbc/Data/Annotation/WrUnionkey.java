package com.woerry.wrbootjdbc.Data.Annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrUnionkey {
    String name() ;
    String aimtable();
    String aimcol();
    int size() default 0;
    String type() default "";
    String remark() default "";

}
