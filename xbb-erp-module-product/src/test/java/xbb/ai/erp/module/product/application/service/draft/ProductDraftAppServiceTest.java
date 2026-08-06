package xbb.ai.erp.module.product.application.service.draft;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftMetaDTO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductDraftRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDraftAppServiceTest {

    @Test
    void should_save_and_load_draft() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductDraftAppService service = new ProductDraftAppServiceImpl(draftRepository);

        ProductDraftSaveDTO saveDTO = saveDto("SPU-001", "商品A", "草稿A", 100L);
        Long draftId = service.saveDraft(saveDTO).getDraftId();

        ProductDraftLoadDTO loadDTO = new ProductDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftId(draftId);
        ProductDraftDetailVO detail = service.loadDraft(loadDTO);

        assertNotNull(draftId);
        assertEquals("草稿A", detail.getDraftMeta().getDraftName());
        assertEquals("SPU-001", detail.getDetail().getMainData().getMain().getSpuCode());
        assertEquals("SKU-001", detail.getDetail().getMainData().getSkus().get(0).getSkuCode());
    }

    @Test
    void should_require_corpid_for_draft_list() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductDraftAppService service = new ProductDraftAppServiceImpl(draftRepository);

        ProductDraftListDTO dto = new ProductDraftListDTO();

        BizException ex = assertThrows(BizException.class, () -> service.draftList(dto));

        assertEquals("公司不能为空", ex.getMessage());
    }

    @Test
    void should_require_draft_id_for_load_draft() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductDraftAppService service = new ProductDraftAppServiceImpl(draftRepository);

        ProductDraftLoadDTO dto = new ProductDraftLoadDTO();
        dto.setCorpid("corp-001");

        BizException ex = assertThrows(BizException.class, () -> service.loadDraft(dto));

        assertEquals("draftId不能为空", ex.getMessage());
    }

    private ProductDraftSaveDTO saveDto(String spuCode, String spuName, String draftName, Long updatedTime) {
        ProductDraftSaveDTO dto = new ProductDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        ProductMainDTO main = new ProductMainDTO();
        main.setSpuCode(spuCode);
        main.setSpuName(spuName);
        main.setEnableSpec(0);
        main.setProductType("NORMAL");
        main.setSpuEnableStatus(1);
        dto.setMain(main);
        ProductSkuItemDTO sku = new ProductSkuItemDTO();
        sku.setSkuCode("SKU-001");
        sku.setSkuName("默认SKU");
        sku.setSpecSignature("DEFAULT");
        sku.setSpecSnapshot("DEFAULT");
        sku.setSkuEnableStatus(1);
        sku.setListingStatus(1);
        dto.setSkus(List.of(sku));
        ProductDraftMetaDTO draftMeta = new ProductDraftMetaDTO();
        draftMeta.setDraftName(draftName);
        draftMeta.setUpdatedTime(updatedTime);
        dto.setDraftMeta(draftMeta);
        return dto;
    }
}
