package xbb.ai.erp.module.customer.domain.field;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import xbb.ai.erp.module.customer.domain.config.CustomerFieldConfiguration;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldFactoryConfigurationStructureTest {

    @Test
    void should_expose_customer_field_factory_bean() throws NoSuchMethodException {
        Method method = CustomerFieldConfiguration.class.getDeclaredMethod("customerFieldFactory", List.class);

        assertTrue(method.isAnnotationPresent(Bean.class));
        assertEquals(CustomerFieldFactory.class, method.getReturnType());
    }
}
