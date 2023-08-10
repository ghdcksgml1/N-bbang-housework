package com.heachi.external.config;

import com.heachi.external.annotation.ExternalClients;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Component
public class ExternalClientsPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Reflections ref = new Reflections("com.heachi.external");

        for (Class<?> clazz : ref.getTypesAnnotatedWith(ExternalClients.class)) {
            ExternalClients annotation = clazz.getAnnotation(ExternalClients.class);
            
            String baseUrl = environment.getProperty(annotation.baseUrl());
            baseUrl = (baseUrl != null) ? baseUrl : annotation.baseUrl();

            WebClient webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .build();

            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
            Object externalClientsBean = factory.createClient(clazz);

            beanFactory.registerSingleton(clazz.getSimpleName(), externalClientsBean);

            log.info(">>>> Success External Clients : {}, baseUrl : {}", clazz.getSimpleName(), baseUrl);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
