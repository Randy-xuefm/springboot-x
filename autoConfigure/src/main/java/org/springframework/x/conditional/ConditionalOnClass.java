package org.springframework.x.conditional;

import org.springframework.context.annotation.Conditional;

/**
 * Created by fenming.xue on 2019/8/31.
 */
@Conditional(ConditionOnClass.class)
public @interface ConditionalOnClass {

    Class<?>[] value() default {};

    String[] name() default {};
}
