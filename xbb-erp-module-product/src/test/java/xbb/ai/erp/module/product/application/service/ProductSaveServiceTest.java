package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.application.service.impl.ProductAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppService;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSpuRepository;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductSaveServiceTest {

    @Test
    void should_save_single_spec_product_with_exactly_one_sku() {
        InMemoryProductSpuRepository spuRepository = new InMemoryProductSpuRepository();
        InMemoryProductSkuRepository skuRepository = new InMemoryProductSkuRepository();
        ProductSaveAppServiceImpl service = new ProductSaveAppServiceImpl(spuRepository, skuRepository);

        Long spuId = service.save(singleSpecDto());

        assertNotNull(spuId);
        assertEquals(1, spuRepository.all().size());
        assertEquals(1, skuRepository.all().size());
        assertEquals(spuId, skuRepository.all().get(0).getSpuId());
    }

    @Test
    void should_sync_skus_for_update_with_delete_update_and_insert() {
        InMemoryProductSpuRepository spuRepository = new InMemoryProductSpuRepository();
        InMemoryProductSkuRepository skuRepository = new InMemoryProductSkuRepository();
        ProductSpu spu = new ProductSpu();
        spu.setId(101L);
        spu.setCorpid("corp-001");
        spu.setSpuCode("SPU-OLD");
        spu.setSpuName("旧商品");
        spu.setEnableSpec(1);
        spu.setEnableStatus(1);
        spuRepository.seed(spu);

        ProductSku keepSku = new ProductSku();
        keepSku.setId(1001L);
        keepSku.setCorpid("corp-001");
        keepSku.setSpuId(101L);
        keepSku.setSkuCode("SKU-KEEP");
        keepSku.setSkuName("旧保留SKU");
        keepSku.setSpecSignature("OLD-KEEP");
        skuRepository.seed(keepSku);

        ProductSku removeSku = new ProductSku();
        removeSku.setId(1002L);
        removeSku.setCorpid("corp-001");
        removeSku.setSpuId(101L);
        removeSku.setSkuCode("SKU-REMOVE");
        removeSku.setSkuName("旧删除SKU");
        removeSku.setSpecSignature("OLD-REMOVE");
        skuRepository.seed(removeSku);

        ProductSaveAppServiceImpl service = new ProductSaveAppServiceImpl(spuRepository, skuRepository);

        Long spuId = service.save(updateDto());

        assertEquals(101L, spuId);
        assertEquals(1, spuRepository.all().size());
        assertEquals(2, skuRepository.all().size());
        assertTrue(skuRepository.findById("corp-001", 1002L) == null);
        ProductSku updated = skuRepository.findById("corp-001", 1001L);
        assertEquals("SKU-KEEP-NEW", updated.getSkuCode());
        assertEquals("NEW-KEEP", updated.getSpecSignature());
        ProductSku inserted = skuRepository.all().stream().filter(item -> !Long.valueOf(1001L).equals(item.getId())).findFirst().orElseThrow();
        assertEquals("SKU-NEW", inserted.getSkuCode());
        assertEquals(101L, inserted.getSpuId());
    }

    @Test
    void should_delegate_save_and_submit_from_admin_service() {
        RecordingProductSaveAppService saveAppService = new RecordingProductSaveAppService();
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(null, null, saveAppService, null);
        ProductSubmitSaveDTO dto = submitDto();

        BaseVO result = service.saveAndSubmit(dto);

        assertNotNull(result);
        assertEquals(1, result.getOk());
        assertTrue(saveAppService.called);
        assertEquals(dto, saveAppService.receivedDto);
    }

    private ProductSaveDTO singleSpecDto() {
        ProductSaveDTO dto = baseSaveDto();
        dto.getMain().setEnableSpec(0);
        dto.setSkus(List.of(skuItem(null, "SKU-001", "默认规格", "DEFAULT")));
        return dto;
    }

    private ProductSaveDTO updateDto() {
        ProductSaveDTO dto = baseSaveDto();
        dto.getMain().setSpuId(101L);
        dto.getMain().setSpuCode("SPU-001");
        dto.getMain().setSpuName("更新后商品");
        dto.getMain().setEnableSpec(1);
        dto.setSkus(List.of(
            skuItem(1001L, "SKU-KEEP-NEW", "更新后SKU", "NEW-KEEP"),
            skuItem(null, "SKU-NEW", "新增SKU", "NEW-ADD")
        ));
        return dto;
    }

    private ProductSubmitSaveDTO submitDto() {
        ProductSubmitSaveDTO dto = new ProductSubmitSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(new ProductMainDTO());
        dto.setSkus(List.of(skuItem(null, "SKU-001", "默认规格", "DEFAULT")));
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

    private static class RecordingProductSaveAppService implements ProductSaveAppService {
        private boolean called;
        private ProductSubmitSaveDTO receivedDto;

        @Override
        public Long save(ProductSaveDTO dto) {
            return 1L;
        }

        @Override
        public BaseVO saveAndSubmit(ProductSubmitSaveDTO dto) {
            this.called = true;
            this.receivedDto = dto;
            return new BaseVO();
        }
    }
}
