package org.springframework.x.conditional;

/**
 * Created by fenming.xue on 2020/5/2.
 */
public @interface ConditionalOnBean {
    String[] name() default {};
}
