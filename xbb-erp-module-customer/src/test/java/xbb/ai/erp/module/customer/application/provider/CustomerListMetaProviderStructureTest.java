package xbb.ai.erp.module.customer.application.provider;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerListMetaProviderStructureTest {

    @Test
    void should_require_customer_field_factory_in_constructor() {
        Constructor<?>[] constructors = CustomerListMetaProvider.class.getDeclaredConstructors();

        assertEquals(1, constructors.length);
        assertEquals(1, constructors[0].getParameterCount());
        assertEquals(CustomerFieldFactory.class, constructors[0].getParameterTypes()[0]);
    }

    @Test
    void should_define_customer_list_schema_provider_and_query_adapter() throws Exception {
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.schema.CustomerListSchemaProvider"));
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.schema.CustomerListQueryAdapter"));
    }
}
