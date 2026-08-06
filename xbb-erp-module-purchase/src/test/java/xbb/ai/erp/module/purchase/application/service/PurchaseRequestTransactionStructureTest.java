package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseRequestAdminAppServiceImpl;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseRequestTransactionStructureTest {

    @Test
    void should_add_transaction_boundary_on_purchase_request_write_methods() throws Exception {
        assertTransactional("save", PurchaseRequestSaveDTO.class);
        assertTransactional("saveAndSubmit", PurchaseRequestSubmitSaveDTO.class);
        assertTransactional("delete", BatchBaseDTO.class);
    }

    private void assertTransactional(String methodName, Class<?> parameterType) throws Exception {
        Method method = PurchaseRequestAdminAppServiceImpl.class.getMethod(methodName, parameterType);
        Transactional transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional, methodName + " 缺少事务注解");
        assertArrayEquals(new Class<?>[]{Exception.class}, Arrays.stream(transactional.rollbackFor()).toArray(Class[]::new), methodName + " 必须声明 rollbackFor = Exception.class");
    }
}
