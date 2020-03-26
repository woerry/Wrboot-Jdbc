package com.woerry.wrbootjdbc.Data.Annotation;

import com.woerry.wrbootjdbc.Data.Constant.ColType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrColumn {
    String name() ;
    int size() default 1;
    ColType colType() ;
    String colTypeArgs() default "";//在coltype之后的参数，比如DECIMAL(3，2)的 (3，2)，括号要英文状态
    boolean iskey() default false;
    boolean isforeignkey() default false;
    String fktable() default "";
    String fkcolumn() default "";
    String remark() default "";
    int sequence() default 0;
}
