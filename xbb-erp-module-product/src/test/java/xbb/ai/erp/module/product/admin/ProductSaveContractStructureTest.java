package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductSaveContractStructureTest {

    @Test
    void should_make_product_save_dto_extend_base_dto_and_hold_spu_plus_skus() throws Exception {
        assertEquals(BaseDTO.class, ProductSaveDTO.class.getSuperclass());

        Field main = ProductSaveDTO.class.getDeclaredField("main");
        Field skus = ProductSaveDTO.class.getDeclaredField("skus");
        assertEquals(ProductMainDTO.class, main.getType());
        assertEquals(List.class, skus.getType());
    }

    @Test
    void should_make_submit_and_draft_extend_save_contract() {
        assertEquals(ProductSaveDTO.class, ProductSubmitSaveDTO.class.getSuperclass());
        assertEquals(ProductSaveDTO.class, ProductDraftSaveDTO.class.getSuperclass());
    }

    @Test
    void should_define_product_sku_item_dto() throws Exception {
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("skuCode").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("skuName").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("specSignature").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("specSnapshot").getType());
    }

    @Test
    void should_make_product_detail_wrap_save_item_vo() throws Exception {
        Field mainData = ProductDetailVO.class.getDeclaredField("mainData");

        assertEquals(ProductSaveItemVO.class, mainData.getType());
    }
}
