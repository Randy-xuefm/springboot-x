package org.springframework.x.autoconfigure;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by fenming.xue on 2020/6/23.
 * EnableAutoConfiguration parser
 * @see EnableAutoConfiguration
 */
public class EnableAutoConfigurationSelector implements DeferredImportSelector, BeanClassLoaderAware {

    private ClassLoader classLoader;

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> configList = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);
        configList = removeDuplicate(configList);

        return configList.toArray(new String[0]);
    }

    /**
     * 去重复数据
     * @param configList
     * @return
     */
    private List<String> removeDuplicate(List<String> configList){
        return new ArrayList<>(new HashSet<>(configList));
    }

    @Override
    public Class<? extends Group> getImportGroup() {
        return null;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
