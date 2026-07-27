package xbb.ai.erp.module.customer.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldRule;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;

import java.util.List;

@Configuration
public class CustomerFieldConfiguration {

    @Bean
    public CustomerFieldFactory customerFieldFactory(List<CustomerFieldRule> rules) {
        return new DefaultCustomerFieldFactory(List.copyOf(rules));
    }
}
