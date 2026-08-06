package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.domain.model.ProductSku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductBusinessSelectQueryServiceTest {

    @Test
    void should_support_product_business_select_queries() {
        InMemoryProductSkuRepository skuRepository = new InMemoryProductSkuRepository();
        skuRepository.seed(buildSku(1001L, "demo-corp", "SKU-001", "红色款", 1, 1, "MUG", "6900001", "红色 / L"));
        skuRepository.seed(buildSku(1002L, "demo-corp", "SKU-002", "蓝色款", 1, 1, "CUP", "6900002", null));
        skuRepository.seed(buildSku(1003L, "demo-corp", "SKU-003", "禁用款", 1, 0, "BAN", "6900003", "黑色 / S"));
        skuRepository.seed(buildSku(1004L, "demo-corp", "SKU-004", "不可采购款", 0, 1, "STOP", "6900004", "白色 / XS"));
        ProductQueryAppServiceImpl service = ProductQueryAppServiceImpl.forTesting(null, skuRepository);

        ProductBusinessSelectQueryDTO queryDTO = new ProductBusinessSelectQueryDTO();
        queryDTO.setCorpid("demo-corp");
        queryDTO.setKeyword("蓝色");
        queryDTO.setBusinessCode("PURCHASE_ORDER");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);

        List<ProductBusinessSelectOptionVO> quickSearch = service.businessSelectQuickSearch(queryDTO);
        ListBaseVO<ProductBusinessSelectOptionVO> dialogSearch = service.businessSelectDialogSearch(queryDTO);

        ProductBusinessSelectQueryDTO byIdDTO = new ProductBusinessSelectQueryDTO();
        byIdDTO.setCorpid("demo-corp");
        byIdDTO.setId(1001L);
        byIdDTO.setBusinessCode("PURCHASE_ORDER");
        ProductBusinessSelectOptionVO byId = service.businessSelectGetById(byIdDTO);

        assertEquals(1, quickSearch.size());
        assertEquals(1002L, quickSearch.get(0).getId());
        assertEquals("SKU-002 蓝色款", quickSearch.get(0).getLabel());
        Map<String, Object> expectedLinePatch = new HashMap<>();
        expectedLinePatch.put("skuId", 1002L);
        expectedLinePatch.put("skuCodeSnapshot", "SKU-002");
        expectedLinePatch.put("skuNameSnapshot", "蓝色款");
        expectedLinePatch.put("specSnapshot", null);
        expectedLinePatch.put("purchaseUnitId", 1L);
        expectedLinePatch.put("warehouseId", 1L);
        expectedLinePatch.put("orderQty", java.math.BigDecimal.ONE);
        expectedLinePatch.put("grossPrice", java.math.BigDecimal.ZERO);
        expectedLinePatch.put("netPrice", java.math.BigDecimal.ZERO);
        expectedLinePatch.put("taxRate", java.math.BigDecimal.ZERO);
        expectedLinePatch.put("taxAmount", java.math.BigDecimal.ZERO);
        expectedLinePatch.put("grossAmount", java.math.BigDecimal.ZERO);
        expectedLinePatch.put("netAmount", java.math.BigDecimal.ZERO);
        assertEquals(expectedLinePatch, quickSearch.get(0).getLinePatch());
        assertEquals(1, dialogSearch.getList().size());
        assertEquals(7, dialogSearch.getHeadList().size());
        assertNotNull(dialogSearch.getPageHelper());
        assertEquals("SKU-001 红色款", byId.getLabel());
        assertEquals(1L, byId.getLinePatch().get("purchaseUnitId"));
    }

    private ProductSku buildSku(Long id, String corpid, String skuCode, String skuName, Integer canPurchase, Integer enableStatus, String mnemonicCode, String mainBarcode, String specSnapshot) {
        ProductSku sku = new ProductSku();
        sku.setId(id);
        sku.setCorpid(corpid);
        sku.setSkuCode(skuCode);
        sku.setSkuName(skuName);
        sku.setCanPurchase(canPurchase);
        sku.setEnableStatus(enableStatus);
        sku.setMnemonicCode(mnemonicCode);
        sku.setMainBarcode(mainBarcode);
        sku.setSpecSnapshot(specSnapshot);
        return sku;
    }
}
