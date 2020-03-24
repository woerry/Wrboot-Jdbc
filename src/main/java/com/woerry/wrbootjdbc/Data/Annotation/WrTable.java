package com.woerry.wrbootjdbc.Data.Annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrTable {
String name() ;
String database() default "";
int keynum() default 0;
}
