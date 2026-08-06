package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.FakeProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.FakeProductSpuRepository;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDetailServiceTest {

    @Test
    void should_build_product_detail_with_spu_and_all_skus() {
        ProductSpu spu = new ProductSpu();
        spu.setId(101L);
        spu.setCorpid("c1");
        spu.setSpuCode("SPU-101");
        spu.setSpuName("测试商品");
        spu.setEnableSpec(1);
        spu.setEnableStatus(1);

        ProductSku sku1 = new ProductSku();
        sku1.setId(1001L);
        sku1.setCorpid("c1");
        sku1.setSpuId(101L);
        sku1.setSkuCode("SKU-1");
        sku1.setSkuName("红色款");
        sku1.setSpecSignature("color:red");
        sku1.setSpecSnapshot("{\"color\":\"red\"}");

        ProductSku sku2 = new ProductSku();
        sku2.setId(1002L);
        sku2.setCorpid("c1");
        sku2.setSpuId(101L);
        sku2.setSkuCode("SKU-2");
        sku2.setSkuName("蓝色款");
        sku2.setSpecSignature("color:blue");
        sku2.setSpecSnapshot("{\"color\":\"blue\"}");

        ProductQueryAppServiceImpl service = ProductQueryAppServiceImpl.forTesting(
            new FakeProductSpuRepository(List.of(spu)),
            new FakeProductSkuRepository(List.of(sku1, sku2))
        );

        ProductDetailVO detail = service.detail(idBaseDTO("c1", 101L));

        assertEquals(101L, detail.getMainData().getMain().getSpuId());
        assertEquals(2, detail.getMainData().getSkus().size());
        assertEquals("SKU-1", detail.getMainData().getSkus().get(0).getSkuCode());
        assertEquals("SKU-2", detail.getMainData().getSkus().get(1).getSkuCode());
    }

    private IdBaseDTO idBaseDTO(String corpid, Long id) {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid(corpid);
        dto.setId(id);
        return dto;
    }
}
