package org.springframework.x.autoconfigure;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return AutoConfigurationGroup.class;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static class AutoConfigurationGroup implements Group,BeanClassLoaderAware {

        private ClassLoader classLoader;

        @Override
        public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
            //验证metadata是否正确.
            Assert.state(selector instanceof EnableAutoConfigurationSelector,"AutoConfigurationGroup只能用在EnableAutoConfigurationSelector");
        }

        @Override
        public Iterable<Entry> selectImports() {
            List<String> configList = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);

            if(configList.isEmpty()){
                return null;
            }
            configList = removeDuplicate(configList);
            //自然排序
            configList.sort(String::compareToIgnoreCase);
            List<AutoConfigurationEnity> enityList = new ArrayList<>();
            configList.forEach(name -> enityList.add(new AutoConfigurationEnity(name)));
            //@ConfigurationOrder
            enityList.sort(AutoConfigurationEnity::compareTo);
            //@ConfigurationBefore,@ConfigurationAfter
            List<AutoConfigurationEnity> resultList = handleBeforeAndAfter(enityList);

            return resultList.stream().map(entity -> new Entry(AnnotationMetadata.introspect(entity.source),entity.sourceClassName)).collect(Collectors.toList());
        }

        /**
         * 去重复数据
         * @param configList
         * @return
         */
        private List<String> removeDuplicate(List<String> configList){
            return new ArrayList<>(new HashSet<>(configList));
        }

        private List<AutoConfigurationEnity> handleBeforeAndAfter(List<AutoConfigurationEnity> enityList){
            Set<AutoConfigurationEnity> sorted = new LinkedHashSet<>(enityList.size(),1);
            List<AutoConfigurationEnity> toSort = new ArrayList<>(enityList);
            Set<AutoConfigurationEnity> sorting = new LinkedHashSet<>();

            while (!toSort.isEmpty()){
                sort4BeforeAfter(enityList,sorted,toSort,sorting,null);

            }

            return new ArrayList<>(sorted);
        }

        private void sort4BeforeAfter(List<AutoConfigurationEnity> all, Set<AutoConfigurationEnity> sorted, List<AutoConfigurationEnity> toSort, Set<AutoConfigurationEnity> sorting, AutoConfigurationEnity current){
            if(current == null){
                current = toSort.remove(0);
            }
            sorting.add(current);

            for (AutoConfigurationEnity enity : current.getAfter(all)) {
                Assert.state(!current.equals(enity),"循环引用");

                if(!sorted.contains(enity) && toSort.contains(enity)){
                    sort4BeforeAfter(all,sorted,toSort,sorting,enity);
                }
            }
            sorting.remove(current);
            sorted.add(current);
        }

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }
    }

    static class AutoConfigurationEnity {
        private String sourceClassName;
        private Class<?> source;
        private int order;
        private Class<?>[] after;
        private Class<?>[] before;

        @Override
        public boolean equals(Object o) {
            if (this == o){
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }

            AutoConfigurationEnity that = (AutoConfigurationEnity) o;

            return sourceClassName.equals(that.sourceClassName);
        }

        @Override
        public int hashCode() {
            return sourceClassName.hashCode();
        }

        public AutoConfigurationEnity(String sourceClassName){
            this.sourceClassName = sourceClassName;
            initEntity();
        }

        private void initEntity(){
            try {
                Class<?> clazz = Class.forName(this.sourceClassName);
                this.source = clazz;
                Annotation[] annotations = clazz.getAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(ConfigurationOrder.class)) {
                        this.order =((ConfigurationOrder)annotation).value();
                    } else if(annotation.annotationType().equals(ConfigurationBefore.class)){
                        this.before =((ConfigurationBefore)annotation).value();
                    }else if(annotation.annotationType().equals(ConfigurationAfter.class)){
                        this.after =((ConfigurationAfter)annotation).value();
                    }
                }
            } catch (ClassNotFoundException e) {
                //error,class not find
            }

        }

        public int compareTo(AutoConfigurationEnity enity){

            if(this.order != 0){
                return Integer.compare(this.order,enity.order);
            }

            return 1;
        }

        public Set<AutoConfigurationEnity> getAfter(List<AutoConfigurationEnity> all){
            Set<AutoConfigurationEnity> result = new HashSet<>();
            if(this.after != null){
                result.addAll(Arrays.stream(this.after).map(clazz -> new AutoConfigurationEnity(clazz.getName())).collect(Collectors.toSet()));
            }

            for (AutoConfigurationEnity enity : all) {
                if(enity.before != null){
                    result.addAll(Arrays.stream(enity.before)
                                          .filter(clazz -> clazz.equals(this.source))
                                          .map(clazz -> new AutoConfigurationEnity(enity.sourceClassName))
                                          .collect(Collectors.toSet()));
                }
            }

            return result;

        }
    }
}
