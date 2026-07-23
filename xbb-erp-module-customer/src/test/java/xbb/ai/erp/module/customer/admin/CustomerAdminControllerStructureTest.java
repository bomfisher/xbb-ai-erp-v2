package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAdminControllerStructureTest {

    @Test
    void should_declare_six_crud_endpoints() throws Exception {
        Method list = CustomerAdminController.class.getMethod("list", xbb.ai.erp.module.customer.admin.dto.CustomerListDTO.class);
        Method addItem = CustomerAdminController.class.getMethod("addItem", xbb.ai.erp.base.common.dto.BaseDTO.class);
        Method updateItem = CustomerAdminController.class.getMethod("updateItem", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method save = CustomerAdminController.class.getMethod("save", xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO.class);
        Method detail = CustomerAdminController.class.getMethod("detail", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method delete = CustomerAdminController.class.getMethod("delete", xbb.ai.erp.base.common.dto.BatchBaseDTO.class);

        assertNotNull(list);
        assertNotNull(addItem);
        assertNotNull(updateItem);
        assertNotNull(save);
        assertNotNull(detail);
        assertNotNull(delete);
    }
}
