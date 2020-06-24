package org.springframework.x.autoconfigure;

import org.springframework.context.annotation.Configuration;

/**
 * Created by fenming.xue on 2020/6/24.
 */
@Configuration
@ConfigurationAfter(SampleConfigA.class)
public class SampleConfigC {
}
