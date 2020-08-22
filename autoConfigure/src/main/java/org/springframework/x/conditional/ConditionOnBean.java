package org.springframework.x.conditional;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenming.xue on 2020/5/2.
 */
public class ConditionOnBean extends AbstractFilterCondition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnBean.class.getName(), true);
        if (attributes == null) {
            return true;
        }
        List<String> candidates = new ArrayList<>();
        addAll(candidates, attributes.get("name"));

        return matches(candidates,context);

    }

    private boolean matches(List<String> beanNameList,ConditionContext context){
        boolean result = true;
        if(beanNameList == null || beanNameList.isEmpty()){
            return result;
        }
        for (String beanName : beanNameList) {
            result = result && context.getRegistry().isBeanNameInUse(beanName);
        }

        return result;
    }
}
