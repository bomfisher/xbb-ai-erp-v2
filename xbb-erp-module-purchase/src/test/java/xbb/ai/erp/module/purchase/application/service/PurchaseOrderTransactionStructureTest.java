package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseOrderAdminAppServiceImpl;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseOrderTransactionStructureTest {

    @Test
    void should_add_transaction_boundary_on_purchase_order_write_methods() throws Exception {
        assertTransactional("save", PurchaseOrderSaveDTO.class);
        assertTransactional("saveAndSubmit", PurchaseOrderSubmitSaveDTO.class);
        assertTransactional("delete", BatchBaseDTO.class);
    }

    private void assertTransactional(String methodName, Class<?> parameterType) throws Exception {
        Method method = PurchaseOrderAdminAppServiceImpl.class.getMethod(methodName, parameterType);
        Transactional transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional, methodName + " 缺少事务注解");
        assertArrayEquals(new Class<?>[]{Exception.class}, Arrays.stream(transactional.rollbackFor()).toArray(Class[]::new), methodName + " 必须声明 rollbackFor = Exception.class");
    }
}
