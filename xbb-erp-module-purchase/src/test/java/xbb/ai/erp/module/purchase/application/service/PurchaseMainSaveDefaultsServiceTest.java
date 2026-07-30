package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseOrderAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseRequestAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseMainSaveDefaultsServiceTest {

    @Test
    void should_reject_blank_corpid_for_purchase_request_list() {
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(new InMemoryPurchaseRequestRepository());
        PurchaseRequestListDTO dto = new PurchaseRequestListDTO();

        BizException exception = assertThrows(BizException.class, () -> service.list(dto));

        assertEquals("公司不能为空", exception.getMessage());
    }

    @Test
    void should_reject_null_id_for_purchase_request_update_item() {
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(new InMemoryPurchaseRequestRepository());
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.updateItem(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_reject_null_id_for_purchase_request_detail() {
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(new InMemoryPurchaseRequestRepository());
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.detail(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_reject_empty_id_list_for_purchase_request_delete() {
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(new InMemoryPurchaseRequestRepository());
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.delete(dto));

        assertEquals("idList不能为空", exception.getMessage());
    }

    @Test
    void should_reject_blank_corpid_for_purchase_order_list() {
        PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(new InMemoryPurchaseOrderRepository());
        PurchaseOrderListDTO dto = new PurchaseOrderListDTO();

        BizException exception = assertThrows(BizException.class, () -> service.list(dto));

        assertEquals("公司不能为空", exception.getMessage());
    }

    @Test
    void should_reject_null_id_for_purchase_order_update_item() {
        PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(new InMemoryPurchaseOrderRepository());
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.updateItem(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_reject_null_id_for_purchase_order_detail() {
        PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(new InMemoryPurchaseOrderRepository());
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.detail(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_reject_empty_id_list_for_purchase_order_delete() {
        PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(new InMemoryPurchaseOrderRepository());
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.delete(dto));

        assertEquals("idList不能为空", exception.getMessage());
    }

    @Test
    void should_apply_insert_defaults_for_purchase_request() {
        InMemoryPurchaseRequestRepository repository = new InMemoryPurchaseRequestRepository();
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(repository);

        PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
        main.setPurchaseOrgId(10L);
        main.setRequestNo("PR-001");
        main.setApplicantId("emp-001");

        PurchaseRequestSaveDTO dto = new PurchaseRequestSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        Long savedId = service.save(dto);
        PurchaseRequest saved = repository.all().get(0);

        assertEquals(savedId, saved.getId());
        assertEquals("1", saved.getBizStatus());
        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }

    @Test
    void should_keep_explicit_insert_fields_when_purchase_request_values_are_provided() {
        InMemoryPurchaseRequestRepository repository = new InMemoryPurchaseRequestRepository();
        PurchaseRequestAdminAppServiceImpl service = new PurchaseRequestAdminAppServiceImpl(repository);

        PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
        main.setPurchaseOrgId(10L);
        main.setRequestNo("PR-002");
        main.setBizStatus("MANUAL");
        main.setVersion(8);
        main.setDeleted(0);
        main.setAddTime(100L);
        main.setUpdateTime(200L);
        main.setCreatorId("creator-x");
        main.setModifyId("modifier-y");

        PurchaseRequestSaveDTO dto = new PurchaseRequestSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        service.save(dto);
        PurchaseRequest saved = repository.all().get(0);

        assertEquals("MANUAL", saved.getBizStatus());
        assertEquals(8, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals(100L, saved.getAddTime());
        assertEquals(200L, saved.getUpdateTime());
        assertEquals("creator-x", saved.getCreatorId());
        assertEquals("modifier-y", saved.getModifyId());
    }

    @Test
    void should_apply_insert_defaults_for_purchase_order() {
        InMemoryPurchaseOrderRepository repository = new InMemoryPurchaseOrderRepository();
        PurchaseOrderAdminAppServiceImpl service = new PurchaseOrderAdminAppServiceImpl(repository);

        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setPurchaseOrgId(10L);
        main.setOrderNo("PO-001");
        main.setVendorId(20L);
        main.setVendorNameSnapshot("杭州供应商");

        PurchaseOrderSaveDTO dto = new PurchaseOrderSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        Long savedId = service.save(dto);
        PurchaseOrder saved = repository.all().get(0);

        assertEquals(savedId, saved.getId());
        assertEquals("1", saved.getBizStatus());
        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }
}
