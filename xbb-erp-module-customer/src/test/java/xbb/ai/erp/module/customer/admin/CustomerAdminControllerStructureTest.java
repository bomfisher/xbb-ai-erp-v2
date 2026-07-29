package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAdminControllerStructureTest {

    @Test
    void should_declare_customer_endpoints_with_draft_flow() throws Exception {
        Method list = CustomerAdminController.class.getMethod("list", xbb.ai.erp.module.customer.admin.dto.CustomerListDTO.class);
        Method addItem = CustomerAdminController.class.getMethod("addItem", xbb.ai.erp.base.common.dto.BaseDTO.class);
        Method updateItem = CustomerAdminController.class.getMethod("updateItem", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method saveDraft = CustomerAdminController.class.getMethod("saveDraft", xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO.class);
        Method saveAndSubmit = CustomerAdminController.class.getMethod("saveAndSubmit", xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO.class);
        Method draftList = CustomerAdminController.class.getMethod("draftList", xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO.class);
        Method loadDraft = CustomerAdminController.class.getMethod("loadDraft", xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO.class);
        Method detail = CustomerAdminController.class.getMethod("detail", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method delete = CustomerAdminController.class.getMethod("delete", xbb.ai.erp.base.common.dto.BatchBaseDTO.class);

        assertNotNull(list);
        assertNotNull(addItem);
        assertNotNull(updateItem);
        assertNotNull(saveDraft);
        assertNotNull(saveAndSubmit);
        assertNotNull(draftList);
        assertNotNull(loadDraft);
        assertNotNull(detail);
        assertNotNull(delete);
    }
}
