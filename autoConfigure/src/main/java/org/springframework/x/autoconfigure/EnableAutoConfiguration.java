package org.springframework.x.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fenming.xue on 2020/6/23.
 * 自动配置,注入spring Bean
 * @see org.springframework.context.annotation.Configuration
 *
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableAutoConfigurationSelector.class)
public @interface EnableAutoConfiguration {

}
