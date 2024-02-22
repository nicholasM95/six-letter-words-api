package be.nicholasmeyers.word.adapter.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

@Configuration
public class WordBeanConfig {

    @Bean
    static BeanFactoryPostProcessor WordBeanFactoryPostProcessor(ApplicationContext beanRegistry) {
        return beanFactory ->
                wordApplicationContext(
                        (BeanDefinitionRegistry) ((GenericApplicationContext) beanRegistry)
                                .getBeanFactory());
    }

    static void wordApplicationContext(BeanDefinitionRegistry beanRegistry) {
        ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanRegistry);
        beanDefinitionScanner.addIncludeFilter(addUseCaseFilter());
        beanDefinitionScanner.scan(
                "be.nicholasmeyers.word.usecase",
                "be.nicholasmeyers.word.domain",
                "be.nicholasmeyers.word.adapter.controller",
                "be.nicholasmeyers.word.adapter.repository",
                "be.nicholasmeyers.word.adapter.storage"
        );
    }

    static TypeFilter addUseCaseFilter() {
        return (MetadataReader mr, MetadataReaderFactory mrf) -> mr.getClassMetadata()
                .getClassName()
                .endsWith("UseCase") ||
                mr.getClassMetadata()
                        .getClassName()
                        .endsWith("Factory");
    }
}
