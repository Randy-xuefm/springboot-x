package org.springframework.x.autoconfigure;

import org.springframework.core.Ordered;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fenming.xue on 2020/6/24.
 * 自动装配支持排序,order值越小,排在最前面.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationOrder {
    int value() default Ordered.HIGHEST_PRECEDENCE;
}
