package xbb.ai.erp.module.demo.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.module.demo.application.field.DefaultDemoFieldFactory;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;

@Configuration
public class DemoFieldConfiguration {
    @Bean
    public DemoFieldFactory demoFieldFactory() {
        return new DefaultDemoFieldFactory();
    }
}
