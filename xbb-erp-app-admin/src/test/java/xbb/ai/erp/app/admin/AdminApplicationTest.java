package xbb.ai.erp.app.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.admin.vo.ProductVO;
import xbb.ai.erp.module.product.app.service.ProductAppService;
import xbb.ai.erp.module.product.app.service.ProductBrandAppService;
import xbb.ai.erp.module.product.app.service.ProductCategoryAppService;
import xbb.ai.erp.module.product.app.service.ProductUnitAppService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCategoryAppService productCategoryAppService;

    @MockBean
    private ProductBrandAppService productBrandAppService;

    @MockBean
    private ProductUnitAppService productUnitAppService;

    @MockBean
    private ProductAppService productAppService;

    @Test
    void contextLoads() {
    }

    @Test
    void should_expose_user_info_endpoint_under_erp_v1_domain_path() throws Exception {
        mockMvc.perform(get("/erp/v1/user/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    void should_expose_product_category_endpoints() throws Exception {
        ProductCategoryVO categoryVO = new ProductCategoryVO();
        categoryVO.setId(1L);
        given(productCategoryAppService.list(any())).willReturn(List.of());
        given(productCategoryAppService.detail(anyString(), anyLong())).willReturn(categoryVO);
        given(productCategoryAppService.create(any())).willReturn(1L);

        mockMvc.perform(get("/erp/v1/product/category/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/erp/v1/product/category/detail")
                .param("corpid", "corp-001")
                .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(post("/erp/v1/product/category/create")
                .param("corpid", "corp-001")
                .param("userId", "user-001")
                .param("categoryCode", "CAT-001")
                .param("categoryName", "分类1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void should_expose_product_brand_and_unit_endpoints() throws Exception {
        given(productBrandAppService.list(any())).willReturn(List.of());
        given(productUnitAppService.list(any())).willReturn(List.of());

        mockMvc.perform(get("/erp/v1/product/brand/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/erp/v1/product/unit/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    void should_expose_product_aggregate_endpoints() throws Exception {
        ProductVO productVO = new ProductVO();
        productVO.setSpuId(11L);
        productVO.setSkuId(22L);
        given(productAppService.detail(any())).willReturn(productVO);
        given(productAppService.listSpu(any())).willReturn(List.of());
        given(productAppService.listSku(any())).willReturn(List.of());
        given(productAppService.listSpuSku(any())).willReturn(List.of());
        given(productAppService.create(any())).willReturn(1L);

        mockMvc.perform(get("/erp/v1/product/detail")
                .param("corpid", "corp-001")
                .param("spuId", "11")
                .param("skuId", "22"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.spuId").value(11))
            .andExpect(jsonPath("$.data.skuId").value(22));

        mockMvc.perform(get("/erp/v1/product/spu/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/erp/v1/product/sku/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/erp/v1/product/spu-sku/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(post("/erp/v1/product/create")
                .param("corpid", "corp-001")
                .param("userId", "user-001")
                .param("spuCode", "SPU-001")
                .param("spuName", "商品1")
                .param("skuCode", "SKU-001")
                .param("skuName", "规格1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data").value(1));
    }
}
