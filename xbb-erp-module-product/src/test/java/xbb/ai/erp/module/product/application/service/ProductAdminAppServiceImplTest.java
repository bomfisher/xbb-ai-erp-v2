package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.application.service.delete.ProductDeleteAppService;
import xbb.ai.erp.module.product.application.service.impl.ProductAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppService;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppServiceImpl;
import xbb.ai.erp.module.product.application.service.draft.ProductDraftAppService;
import xbb.ai.erp.module.product.application.service.draft.ProductDraftAppServiceImpl;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppService;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductDraftRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSpuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProductAdminAppServiceImplTest {

    @Test
    void should_register_product_save_app_service_as_spring_service() {
        assertTrue(ProductSaveAppServiceImpl.class.isAnnotationPresent(Service.class));
    }

    @Test
    void should_wire_admin_and_save_services_in_spring_context() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.registerBean(ProductAdminAppServiceImpl.class);
            context.registerBean(ProductQueryAppService.class, () -> new ProductQueryAppService() {
                @Override
                public ListBaseVO<ProductListItemVO> list(ProductListDTO dto) {
                    return new ListBaseVO<>();
                }

                @Override
                public SaveItemVO<ProductSaveItemVO> addItem() {
                    return new SaveItemVO<>();
                }

                @Override
                public SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto) {
                    return new SaveItemVO<>();
                }

                @Override
                public List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto) {
                    return List.of();
                }

                @Override
                public ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto) {
                    return new ListBaseVO<>();
                }

                @Override
                public ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto) {
                    return new ProductBusinessSelectOptionVO();
                }

                @Override
                public ProductDetailVO detail(IdBaseDTO dto) {
                    return new ProductDetailVO();
                }
            });
            context.registerBean(ProductDeleteAppService.class, () -> new ProductDeleteAppService() {
                @Override
                public void delete(BatchBaseDTO dto) {
                }
            });
            context.registerBean(ProductSpuRepository.class, InMemoryProductSpuRepository::new);
            context.registerBean(ProductSkuRepository.class, InMemoryProductSkuRepository::new);
            context.registerBean(InMemoryProductDraftRepository.class);
            context.registerBean(ProductDraftAppService.class, () -> new ProductDraftAppServiceImpl(context.getBean(InMemoryProductDraftRepository.class)));
            context.registerBean(ProductSaveAppService.class, () -> new ProductSaveAppServiceImpl(
                context.getBean(ProductSpuRepository.class),
                context.getBean(ProductSkuRepository.class),
                context.getBean(InMemoryProductDraftRepository.class)
            ));
            context.refresh();

            assertNotNull(context.getBean(ProductAdminAppService.class));
            assertNotNull(context.getBean(ProductSaveAppService.class));
        }
    }

    @Test
    void should_return_product_save_page_skeleton_for_add_item() {
        ProductQueryAppServiceImpl queryAppService = ProductQueryAppServiceImpl.forTesting(null, null);
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(queryAppService, null, null, null);

        SaveItemVO<ProductSaveItemVO> addItemResult = service.addItem(new BaseDTO());
        ProductDraftSaveVO saveDraftResult = service.saveDraft(new ProductDraftSaveDTO());
        BaseVO saveAndSubmitResult = service.saveAndSubmit(new ProductSubmitSaveDTO());
        List<?> draftListResult = service.draftList(new ProductDraftListDTO());
        ProductDraftDetailVO loadDraftResult = service.loadDraft(new ProductDraftLoadDTO());

        assertNotNull(addItemResult);
        assertNotNull(addItemResult.getHeadList());
        assertTrue(!addItemResult.getHeadList().isEmpty());
        assertNotNull(addItemResult.getData());
        assertNotNull(addItemResult.getData().getMain());
        assertEquals(Integer.valueOf(0), addItemResult.getData().getMain().getEnableSpec());
        assertEquals(List.of(), addItemResult.getData().getSkus());
        assertEquals(1, addItemResult.getHeadList().stream().filter(item -> "main.spuCode".equals(item.getAttr())).findFirst().orElseThrow().getRequired());
        assertEquals(1, addItemResult.getHeadList().stream().filter(item -> "main.enableSpec".equals(item.getAttr())).findFirst().orElseThrow().getRequired());
        assertEquals("19", addItemResult.getHeadList().stream().filter(item -> "main.enableSpec".equals(item.getAttr())).findFirst().orElseThrow().getFieldType());
        assertFalse(addItemResult.getHeadList().stream().filter(item -> "main.enableSpec".equals(item.getAttr())).findFirst().orElseThrow().getItemList().isEmpty());
        assertNotNull(saveDraftResult);
        assertNotNull(saveAndSubmitResult);
        assertEquals(1, saveAndSubmitResult.getOk());
        assertEquals(List.of(), draftListResult);
        assertNotNull(loadDraftResult);
    }

    @Test
    void should_return_product_save_page_data_for_update_item() {
        ProductQueryAppServiceImpl queryAppService = ProductQueryAppServiceImpl.forTesting(
            new xbb.ai.erp.module.product.application.service.support.FakeProductSpuRepository(List.of(buildSpu())),
            new xbb.ai.erp.module.product.application.service.support.FakeProductSkuRepository(List.of(buildSku()))
        );
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(queryAppService, null, null, null);

        SaveItemVO<ProductSaveItemVO> updateItemResult = service.updateItem(idBaseDTO("c1", 101L));

        assertNotNull(updateItemResult);
        assertNotNull(updateItemResult.getHeadList());
        assertTrue(!updateItemResult.getHeadList().isEmpty());
        assertNotNull(updateItemResult.getData());
        assertEquals(101L, updateItemResult.getData().getMain().getSpuId());
        assertEquals(1, updateItemResult.getData().getSkus().size());
        assertEquals("SKU-1", updateItemResult.getData().getSkus().get(0).getSkuCode());
    }

    private IdBaseDTO idBaseDTO(String corpid, Long id) {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid(corpid);
        dto.setId(id);
        return dto;
    }

    private xbb.ai.erp.module.product.domain.model.ProductSpu buildSpu() {
        xbb.ai.erp.module.product.domain.model.ProductSpu spu = new xbb.ai.erp.module.product.domain.model.ProductSpu();
        spu.setId(101L);
        spu.setCorpid("c1");
        spu.setSpuCode("SPU-101");
        spu.setSpuName("测试商品");
        spu.setEnableSpec(1);
        spu.setEnableStatus(1);
        return spu;
    }

    private xbb.ai.erp.module.product.domain.model.ProductSku buildSku() {
        xbb.ai.erp.module.product.domain.model.ProductSku sku = new xbb.ai.erp.module.product.domain.model.ProductSku();
        sku.setId(1001L);
        sku.setCorpid("c1");
        sku.setSpuId(101L);
        sku.setSkuCode("SKU-1");
        sku.setSkuName("红色款");
        sku.setSpecSignature("color:red");
        sku.setSpecSnapshot("{\"color\":\"red\"}");
        return sku;
    }
}
