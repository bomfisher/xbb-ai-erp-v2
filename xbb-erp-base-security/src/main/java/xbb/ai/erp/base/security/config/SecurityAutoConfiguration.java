package xbb.ai.erp.base.security.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@AutoConfiguration(before = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class BaseSecurityAutoConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    new AntPathRequestMatcher("/actuator/**"),
                    new AntPathRequestMatcher("/erp/v1/user/info"),
                    new AntPathRequestMatcher("/erp/v1/job/health")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});
        return httpSecurity.build();
    }
}
