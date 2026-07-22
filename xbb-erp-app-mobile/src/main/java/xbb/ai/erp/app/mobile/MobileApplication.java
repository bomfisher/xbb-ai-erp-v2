package xbb.ai.erp.app.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "xbb.ai.erp")
public class MobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileApplication.class, args);
    }
}
