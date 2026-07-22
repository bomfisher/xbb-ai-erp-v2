package xbb.ai.erp.base.idgen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.base.idgen.DefaultIdGenerator;
import xbb.ai.erp.base.idgen.IdGenerator;

@Configuration
public class IdGenAutoConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new DefaultIdGenerator();
    }
}
