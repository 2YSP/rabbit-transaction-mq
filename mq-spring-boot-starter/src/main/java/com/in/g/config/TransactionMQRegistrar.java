package com.in.g.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


/**
 * @author Ship
 * @date 2020-04-27 16:25
 */
@Deprecated
public class TransactionMQRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionMQRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        try {
//            Map<String, Object> attributesMap = importingClassMetadata.getAnnotationAttributes(EnableTransactionMQ.class.getName(), true);
//            String methodName = EnableTransactionMQ.class.getDeclaredMethods()[0].getName();
//            if (!(Boolean) attributesMap.get(methodName)) {
//                LOG.info("不需要注册Transaction MQ 相关的Bean,value:[{}]", attributesMap.get(methodName));
//                return;
//            }
//            // 注册相关Bean
//            BeanDefinition rabbitTransactionMessageService = BeanDefinitionBuilder.rootBeanDefinition(RabbitTransactionMessageService.class).getBeanDefinition();
//            BeanDefinition transactionMessageManageService = BeanDefinitionBuilder.rootBeanDefinition(TransactionMessageManageService.class).getBeanDefinition();
//            registry.registerBeanDefinition("rabbitTransactionMessageService", rabbitTransactionMessageService);
//            registry.registerBeanDefinition("transactionMessageManageService", transactionMessageManageService);
//            LOG.info("register Bean Definitions success");
//        } catch (Exception e) {
//            LOG.error("register Bean Definitions failed", e);
//        }

    }
}
