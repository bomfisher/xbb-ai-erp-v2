package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationSaveDTO;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseOrderItemAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.impl.PurchasePendingTaskAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseRequestItemAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseSourceRelationAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchasePendingTaskRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseSourceRelationRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseSubEntitySaveDefaultsServiceTest {

    @Test
    void should_apply_insert_defaults_for_purchase_request_item() {
        InMemoryPurchaseRequestItemRepository repository = new InMemoryPurchaseRequestItemRepository();
        PurchaseRequestItemAdminAppServiceImpl service = new PurchaseRequestItemAdminAppServiceImpl(repository);

        PurchaseRequestItemMainDTO main = new PurchaseRequestItemMainDTO();
        main.setRequestId(100L);
        main.setLineNo(1);
        main.setSkuId(1000L);
        main.setRequestQty(BigDecimal.ONE);

        PurchaseRequestItemSaveDTO dto = new PurchaseRequestItemSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        service.save(dto);
        PurchaseRequestItem saved = repository.all().get(0);

        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }

    @Test
    void should_apply_insert_defaults_for_purchase_order_item() {
        InMemoryPurchaseOrderItemRepository repository = new InMemoryPurchaseOrderItemRepository();
        PurchaseOrderItemAdminAppServiceImpl service = new PurchaseOrderItemAdminAppServiceImpl(repository);

        PurchaseOrderItemMainDTO main = new PurchaseOrderItemMainDTO();
        main.setOrderId(200L);
        main.setLineNo(1);
        main.setSkuId(2000L);
        main.setOrderQty(BigDecimal.TEN);

        PurchaseOrderItemSaveDTO dto = new PurchaseOrderItemSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        service.save(dto);
        PurchaseOrderItem saved = repository.all().get(0);

        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }

    @Test
    void should_apply_insert_defaults_for_purchase_pending_task() {
        InMemoryPurchasePendingTaskRepository repository = new InMemoryPurchasePendingTaskRepository();
        PurchasePendingTaskAdminAppServiceImpl service = new PurchasePendingTaskAdminAppServiceImpl(repository);

        PurchasePendingTaskMainDTO main = new PurchasePendingTaskMainDTO();
        main.setPurchaseOrgId(10L);
        main.setTaskNo("PT-001");
        main.setTaskStatus("OPEN");
        main.setSkuId(3000L);
        main.setNeedQty(BigDecimal.ONE);

        PurchasePendingTaskSaveDTO dto = new PurchasePendingTaskSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        service.save(dto);
        PurchasePendingTask saved = repository.all().get(0);

        assertEquals("OPEN", saved.getTaskStatus());
        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }

    @Test
    void should_apply_insert_defaults_for_purchase_source_relation() {
        InMemoryPurchaseSourceRelationRepository repository = new InMemoryPurchaseSourceRelationRepository();
        PurchaseSourceRelationAdminAppServiceImpl service = new PurchaseSourceRelationAdminAppServiceImpl(repository);

        PurchaseSourceRelationMainDTO main = new PurchaseSourceRelationMainDTO();
        main.setSourceDocType("REQUEST");
        main.setSourceDocId(400L);
        main.setTargetDocType("ORDER");
        main.setTargetDocId(500L);
        main.setRelationStatus("LINKED");
        main.setSourceQty(BigDecimal.ONE);

        PurchaseSourceRelationSaveDTO dto = new PurchaseSourceRelationSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);

        service.save(dto);
        PurchaseSourceRelation saved = repository.all().get(0);

        assertEquals("LINKED", saved.getRelationStatus());
        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDeleted());
        assertEquals("user-001", saved.getCreatorId());
        assertEquals("user-001", saved.getModifyId());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }
}
