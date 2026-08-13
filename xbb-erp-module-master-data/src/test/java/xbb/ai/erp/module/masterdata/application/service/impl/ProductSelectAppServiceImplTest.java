package xbb.ai.erp.module.masterdata.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSelectOptionVO;
import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSkuRepository;

class ProductSelectAppServiceImplTest {

    @Test
    void shouldQuickSearchEnabledSkusByKeyword() {
        ProductSkuRepository repository = mock(ProductSkuRepository.class);
        ProductSku sku = sku(10L, 1);
        when(repository.findByCondition(any())).thenReturn(List.of(sku));
        ProductSelectAppServiceImpl service = new ProductSelectAppServiceImpl(repository);

        List<ProductSelectOptionVO> options = service.quickSearch(queryDTO());

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(repository).findByCondition(captor.capture());
        assertEquals("corp-001", captor.getValue().get("corpid"));
        assertEquals(1, captor.getValue().get("enabled"));
        assertEquals("螺丝", captor.getValue().get("keyword"));
        assertEquals(10L, options.getFirst().getId());
        assertEquals("SKU-001", options.getFirst().getCode());
        assertEquals("SKU-001 - 螺丝", options.getFirst().getLabel());
    }

    @Test
    void shouldReturnNullWhenSkuIsDisabled() {
        ProductSkuRepository repository = mock(ProductSkuRepository.class);
        when(repository.findById("corp-001", 10L)).thenReturn(sku(10L, 0));
        ProductSelectAppServiceImpl service = new ProductSelectAppServiceImpl(repository);
        ProductSelectQueryDTO dto = queryDTO();
        dto.setId(10L);

        assertNull(service.getById(dto));
    }

    private ProductSelectQueryDTO queryDTO() {
        ProductSelectQueryDTO dto = new ProductSelectQueryDTO();
        dto.setCorpid("corp-001");
        dto.setBusinessCode("PURCHASE_ORDER");
        dto.setProductType("product-sku");
        dto.setKeyword(" 螺丝 ");
        return dto;
    }

    private ProductSku sku(Long id, Integer enabled) {
        ProductSku sku = new ProductSku();
        sku.setId(id);
        sku.setSkuCode("SKU-001");
        sku.setSkuName("螺丝");
        sku.setEnabled(enabled);
        return sku;
    }
}
