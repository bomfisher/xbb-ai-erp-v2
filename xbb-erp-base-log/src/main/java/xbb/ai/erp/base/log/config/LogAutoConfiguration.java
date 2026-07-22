package xbb.ai.erp.base.log.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogAutoConfiguration {

    @Bean
    public FilterRegistrationBean<LogContextFilter> logContextFilterRegistrationBean() {
        FilterRegistrationBean<LogContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogContextFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
