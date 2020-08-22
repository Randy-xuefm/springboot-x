package org.springframework.x.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by fenming.xue on 2020/5/2.
 */
public abstract class AbstractFilterCondition implements Condition {

    protected static Class<?> resolve(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader != null) {
            return classLoader.loadClass(className);
        }
        return Class.forName(className);

    }

    protected void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }

    protected boolean filter(List<String> candidates,ClassNameFilter filter,ClassLoader classLoader){
        if(candidates.isEmpty()){
            return true;
        }
        boolean result = true;
        for (String candidate : candidates) {
            result = result && filter.matches(candidate,classLoader);
        }
        return result;
    }

    protected enum ClassNameFilter {

        PRESENT {

            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return isPresent(className, classLoader);
            }

        },

        MISSING {

            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return !isPresent(className, classLoader);
            }

        };

        abstract boolean matches(String className, ClassLoader classLoader);

        static boolean isPresent(String className, ClassLoader classLoaderTmp) {
            ClassLoader classLoader = classLoaderTmp;
            if (classLoader == null) {
                classLoader = ClassUtils.getDefaultClassLoader();
            }
            try {
                resolve(className, classLoader);
                return true;
            }
            catch (Exception ex) {
                return false;
            }
        }

    }

}
