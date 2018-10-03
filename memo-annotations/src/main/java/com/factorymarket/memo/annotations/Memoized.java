package com.factorymarket.memo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //class level
@Retention(RetentionPolicy.SOURCE) //we only need it at compile time
public @interface Memoized {
    String debugTag() default "TAG";
}