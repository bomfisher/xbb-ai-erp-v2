package xbb.ai.erp.module.customer.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.application.field.CustomerFieldRule;
import xbb.ai.erp.module.customer.application.field.DefaultCustomerFieldFactory;

import java.util.List;

@Configuration
public class CustomerFieldConfiguration {

    @Bean
    public CustomerFieldFactory customerFieldFactory(List<CustomerFieldRule> rules) {
        return new DefaultCustomerFieldFactory(List.copyOf(rules));
    }
}
