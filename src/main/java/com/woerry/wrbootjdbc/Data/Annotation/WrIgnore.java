package com.woerry.wrbootjdbc.Data.Annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrIgnore {
}
