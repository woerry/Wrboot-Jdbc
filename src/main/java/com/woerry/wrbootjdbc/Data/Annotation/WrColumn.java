package com.woerry.wrbootjdbc.Data.Annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrColumn {
    String name() ;
    int size() default 0;
    String type() default "";
    boolean iskey() default false;
    boolean isforeignkey() default false;
    String fktable() default "";
    String fkcolumn() default "";
    String remark() default "";
    int sequence() default 0;
}
