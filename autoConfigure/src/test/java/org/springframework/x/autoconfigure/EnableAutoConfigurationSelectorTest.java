package org.springframework.x.autoconfigure;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void sort4String(){
        List<String> list = Lists.newArrayList("2","1","112233","3","x","y","z");
        list.sort(String::compareTo);

        assertThat(list).containsExactly("1","112233","2","3","x","y","z");

        Collections.sort(list);
        assertThat(list).containsExactly("1","112233","2","3","x","y","z");
    }

    @Test
    public void groupTest(){
        EnableAutoConfigurationSelector.AutoConfigurationGroup group = new EnableAutoConfigurationSelector.AutoConfigurationGroup();
        Iterable<DeferredImportSelector.Group.Entry> entities = group.selectImports();

        List<String> resultList = Lists.newArrayList(entities).stream().map(DeferredImportSelector.Group.Entry::getImportClassName).collect(Collectors.toList());

        assertThat(resultList).containsExactly(SampleConfigA.class.getName(),SampleConfigB.class.getName(),SampleConfig.class.getName(),SampleConfigC.class.getName());
    }
}