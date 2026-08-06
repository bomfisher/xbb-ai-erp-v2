package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.FakeProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.FakeProductSpuRepository;
import xbb.ai.erp.module.product.domain.model.ProductSpu;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductListServiceTest {

    @Test
    void should_return_product_list_items() {
        ProductSpu spu = new ProductSpu();
        spu.setId(101L);
        spu.setCorpid("c1");
        spu.setSpuCode("SPU-101");
        spu.setSpuName("测试商品");
        spu.setProductType("NORMAL");
        spu.setEnableStatus(1);

        ProductQueryAppServiceImpl service = ProductQueryAppServiceImpl.forTesting(
            new FakeProductSpuRepository(List.of(spu)),
            new FakeProductSkuRepository(List.of())
        );

        ProductListDTO dto = new ProductListDTO();
        dto.setCorpid("c1");
        dto.setPage(1);
        dto.setPageSize(20);

        ListBaseVO<ProductListItemVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("SPU-101", result.getList().get(0).getSpuCode());
        assertNotNull(result.getPageHelper());
    }
}
