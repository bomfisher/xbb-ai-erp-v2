package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.application.service.delete.ProductDeleteAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSpuRepository;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDeleteServiceTest {

    @Test
    void should_reject_empty_id_list_for_delete() {
        ProductDeleteAppServiceImpl service = ProductDeleteAppServiceImpl.forTesting(
            new InMemoryProductSpuRepository(),
            new InMemoryProductSkuRepository()
        );
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("c1");

        BizException exception = assertThrows(BizException.class, () -> service.delete(dto));

        assertEquals("idList不能为空", exception.getMessage());
    }

    @Test
    void should_delete_skus_before_spu() {
        InMemoryProductSpuRepository spuRepository = new InMemoryProductSpuRepository();
        InMemoryProductSkuRepository skuRepository = new InMemoryProductSkuRepository();
        ProductSpu spu = new ProductSpu();
        spu.setId(101L);
        spu.setCorpid("c1");
        spuRepository.seed(spu);
        ProductSku sku = new ProductSku();
        sku.setId(1001L);
        sku.setCorpid("c1");
        sku.setSpuId(101L);
        skuRepository.seed(sku);

        ProductDeleteAppServiceImpl service = ProductDeleteAppServiceImpl.forTesting(spuRepository, skuRepository);
        BatchBaseDTO dto = batchBaseDTO("c1", "u1", List.of(101L));

        service.delete(dto);

        assertEquals(List.of("sku:101", "spu:101"), skuRepository.operationLogWith(spuRepository));
        assertEquals(0, skuRepository.all().size());
        assertEquals(0, spuRepository.all().size());
    }

    private BatchBaseDTO batchBaseDTO(String corpid, String userId, List<Long> idList) {
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid(corpid);
        dto.setUserId(userId);
        dto.setIdList(idList);
        return dto;
    }
}
