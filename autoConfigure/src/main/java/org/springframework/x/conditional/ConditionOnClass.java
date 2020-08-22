package org.springframework.x.conditional;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenming.xue on 2019/8/31.
 */
public class ConditionOnClass extends AbstractFilterCondition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnClass.class.getName(), true);
        if (attributes == null) {
            return true;
        }
        List<String> candidates = new ArrayList<>();
        addAll(candidates, attributes.get("value"));
        addAll(candidates, attributes.get("name"));

        return filter(candidates,ClassNameFilter.PRESENT,context.getClassLoader());
    }


}
