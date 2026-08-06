package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSpuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductSaveValidationTest {

    @Test
    void should_reject_single_spec_with_multiple_skus() {
        ProductSaveAppServiceImpl service = new ProductSaveAppServiceImpl(
            new InMemoryProductSpuRepository(),
            new InMemoryProductSkuRepository()
        );

        BizException ex = assertThrows(BizException.class, () -> service.save(singleSpecWithTwoSkusDto()));

        assertEquals("单规格商品仅允许一条SKU", ex.getMessage());
    }

    @Test
    void should_reject_duplicate_spec_signature_in_multi_spec() {
        ProductSaveAppServiceImpl service = new ProductSaveAppServiceImpl(
            new InMemoryProductSpuRepository(),
            new InMemoryProductSkuRepository()
        );

        BizException ex = assertThrows(BizException.class, () -> service.save(multiSpecWithDuplicateSpecSignatureDto()));

        assertEquals("同一商品下规格签名不允许重复", ex.getMessage());
    }

    @Test
    void should_require_main_required_fields_on_submit() {
        ProductSaveAppServiceImpl service = new ProductSaveAppServiceImpl(
            new InMemoryProductSpuRepository(),
            new InMemoryProductSkuRepository()
        );
        ProductSaveDTO dto = baseSaveDto();
        dto.setSkus(List.of(skuItem(null, "SKU-001", "默认规格", "SPEC-001")));
        dto.getMain().setSpuCode(null);

        BizException ex = assertThrows(BizException.class, () -> service.save(dto));

        assertEquals("商品编码不能为空", ex.getMessage());
    }

    private ProductSaveDTO singleSpecWithTwoSkusDto() {
        ProductSaveDTO dto = baseSaveDto();
        dto.getMain().setEnableSpec(0);
        dto.setSkus(List.of(skuItem(null, "SKU-001", "默认规格", "SPEC-001"), skuItem(null, "SKU-002", "额外规格", "SPEC-002")));
        return dto;
    }

    private ProductSaveDTO multiSpecWithDuplicateSpecSignatureDto() {
        ProductSaveDTO dto = baseSaveDto();
        dto.getMain().setEnableSpec(1);
        dto.setSkus(List.of(skuItem(null, "SKU-001", "红色", "COLOR:RED"), skuItem(null, "SKU-002", "红色重复", "COLOR:RED")));
        return dto;
    }

    private ProductSaveDTO multiSpecWithDuplicateSkuCodeDto() {
        ProductSaveDTO dto = baseSaveDto();
        dto.getMain().setEnableSpec(1);
        dto.setSkus(List.of(skuItem(null, "SKU-001", "红色", "COLOR:RED"), skuItem(null, "SKU-001", "蓝色", "COLOR:BLUE")));
        return dto;
    }

    private ProductSaveDTO baseSaveDto() {
        ProductSaveDTO dto = new ProductSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        ProductMainDTO main = new ProductMainDTO();
        main.setSpuCode("SPU-001");
        main.setSpuName("测试商品");
        main.setProductType("NORMAL");
        main.setSpuEnableStatus(1);
        dto.setMain(main);
        return dto;
    }

    private ProductSkuItemDTO skuItem(Long skuId, String skuCode, String skuName, String specSignature) {
        ProductSkuItemDTO item = new ProductSkuItemDTO();
        item.setSkuId(skuId);
        item.setSkuCode(skuCode);
        item.setSkuName(skuName);
        item.setSpecSignature(specSignature);
        item.setSpecSnapshot(specSignature);
        item.setSkuEnableStatus(1);
        item.setListingStatus(1);
        return item;
    }
}
