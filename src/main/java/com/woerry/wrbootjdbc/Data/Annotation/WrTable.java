package com.woerry.wrbootjdbc.Data.Annotation;

import com.woerry.wrbootjdbc.Data.Constant.DbType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrTable {
String name() ;
DbType dbType();
String database() default "";
int keynum() default 1;
}
