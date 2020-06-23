package org.springframework.x.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fenming.xue on 2020/6/23.
 */
@Configuration
public class SampleConfig {

    @Bean("123")
    public String bean(){
        return "123";
    }
}
