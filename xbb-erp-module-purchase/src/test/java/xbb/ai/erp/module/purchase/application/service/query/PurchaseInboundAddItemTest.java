package xbb.ai.erp.module.purchase.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.purchase.application.field.PurchaseInboundFieldFactory;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseInboundAddItemTest {

    @Test
    void should_return_public_linkage_config_for_new_item() {
        PurchaseInboundFieldFactory fieldFactory = Mockito.mock(PurchaseInboundFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseInboundQueryAppServiceImpl service = new PurchaseInboundQueryAppServiceImpl(
            Mockito.mock(PurchaseInboundRepository.class), Mockito.mock(PurchaseInboundItemRepository.class),
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        Map<String, Object> linkageConfig = service.addItem(dto).getLinkageConfig();

        assertNotNull(linkageConfig);
        assertNotNull(linkageConfig.get("clearRules"));
        assertEquals("main.warehouseId", ((Map<?, ?>) linkageConfig.get("warehouseSync")).get("headerAttr"));
    }
}
