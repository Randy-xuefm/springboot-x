# springboot-x
重写springboot项目,实现自动装配

提升动手能力,不断学习

1. @EnableAutoConfiguration功能实现,包括
    - spring.factories SPI机制发现configuration
    - 实现@ConfigurationOrder,@ConfigurationBefore,@ConfigurationAfter

## 未实现功能 ##

1. @EnableAutoConfiguration Filter机制
2. @EnableAutoConfiguration Listener机制

## 后续计划 ##
1. @ConditionalOnClass,@ConditionOnBean实现
2. @ConfigurationProperties实现