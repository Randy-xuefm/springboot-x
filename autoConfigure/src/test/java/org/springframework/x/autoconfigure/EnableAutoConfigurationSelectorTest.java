package org.springframework.x.autoconfigure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by fenming.xue on 2020/6/23.
 */
class EnableAutoConfigurationSelectorTest {

    private EnableAutoConfigurationSelector enableAutoConfigurationSelector;

    @BeforeEach
    public void before(){
        this.enableAutoConfigurationSelector = new EnableAutoConfigurationSelector();
    }

    @Test
    public void selectImports() {
        String[] configNames = this.enableAutoConfigurationSelector.selectImports(AnnotationMetadata.introspect(SampleConfig.class));
        assertThat(configNames).isNotNull();
        assertThat(configNames).contains(SampleConfig.class.getName());
    }

    @Test
    public void springFactoriesLoader(){
        List<String> names = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, null);
        assertThat(names).contains(SampleConfig.class.getName());
    }

    @Test
    public void enableAutoCOnfigurationTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        assertThat(context.getBean("123")).isNotNull();
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config{

    }
}