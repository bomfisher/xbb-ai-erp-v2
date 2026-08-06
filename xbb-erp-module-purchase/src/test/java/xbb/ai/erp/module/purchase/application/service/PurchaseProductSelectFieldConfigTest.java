package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseOrderAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseRequestAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseProductSelectFieldConfigTest {

    @Test
    void should_build_request_product_select_field_for_add_and_update() {
        PurchaseRequestAdminAppService service = new PurchaseRequestAdminAppServiceImpl(
            new InMemoryPurchaseRequestRepository(),
            new InMemoryPurchaseRequestItemRepository()
        );
        BaseDTO addDTO = new BaseDTO();
        addDTO.setCorpid("demo-corp");
        IdBaseDTO updateDTO = new IdBaseDTO();
        updateDTO.setCorpid("demo-corp");
        updateDTO.setId(1L);

        SaveItemVO<PurchaseRequestSaveItemVO> addItem = service.addItem(addDTO);
        SaveItemVO<PurchaseRequestSaveItemVO> updateItem = service.updateItem(updateDTO);

        assertProductField(addItem.getHeadList());
        assertProductField(updateItem.getHeadList());
    }

    @Test
    void should_build_order_product_select_field_for_add_and_update() {
        PurchaseOrderAdminAppService service = new PurchaseOrderAdminAppServiceImpl(
            new InMemoryPurchaseOrderRepository(),
            new InMemoryPurchaseOrderItemRepository()
        );
        BaseDTO addDTO = new BaseDTO();
        addDTO.setCorpid("demo-corp");
        IdBaseDTO updateDTO = new IdBaseDTO();
        updateDTO.setCorpid("demo-corp");
        updateDTO.setId(1L);

        SaveItemVO<PurchaseOrderSaveItemVO> addItem = service.addItem(addDTO);
        SaveItemVO<PurchaseOrderSaveItemVO> updateItem = service.updateItem(updateDTO);

        assertProductField(addItem.getHeadList());
        assertProductField(updateItem.getHeadList());
    }

    private void assertProductField(List<FieldEntity> headList) {
        FieldEntity field = headList.stream()
            .filter(item -> "items.skuId".equals(item.getAttr()))
            .findFirst()
            .orElseThrow();
        assertEquals("产品", field.getAttrName());
        assertEquals("50", field.getFieldType());
        assertNotNull(field.getProductSelectConfig());
        assertEquals("product-sku", field.getProductSelectConfig().getProductType());
        assertEquals(Boolean.TRUE, field.getProductSelectConfig().getMultiple());
    }
}
