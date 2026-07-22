package xbb.ai.erp.base.persistence.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xbb.ai.erp.base.persistence.handler.AuditMetaObjectHandler;

@Configuration
public class MybatisPlusAutoConfiguration {

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new AuditMetaObjectHandler();
    }
}
