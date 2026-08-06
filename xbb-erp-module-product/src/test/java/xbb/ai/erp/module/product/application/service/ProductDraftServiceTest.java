package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftMetaDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;
import xbb.ai.erp.module.product.application.service.draft.ProductDraftAppServiceImpl;
import xbb.ai.erp.module.product.application.service.impl.ProductAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductDraftRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSpuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductDraftServiceTest {

    @Test
    void should_save_and_load_draft_by_id() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(
            null,
            null,
            new ProductSaveAppServiceImpl(new InMemoryProductSpuRepository(), new InMemoryProductSkuRepository(), draftRepository),
            new ProductDraftAppServiceImpl(draftRepository)
        );

        ProductDraftSaveDTO dto = saveDto("SPU-001", "商品A", "草稿A", 100L);
        Long draftId = service.saveDraft(dto).getDraftId();

        ProductDraftLoadDTO loadDTO = new ProductDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftId(draftId);
        ProductDraftDetailVO loaded = service.loadDraft(loadDTO);

        assertNotNull(draftId);
        assertEquals("草稿A", loaded.getDraftMeta().getDraftName());
        assertEquals("SPU-001", loaded.getDetail().getMainData().getMain().getSpuCode());
    }

    @Test
    void should_return_latest_drafts_first_and_apply_keyword_filter() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(
            null,
            null,
            new ProductSaveAppServiceImpl(new InMemoryProductSpuRepository(), new InMemoryProductSkuRepository(), draftRepository),
            new ProductDraftAppServiceImpl(draftRepository)
        );
        draftRepository.seed(draft("corp-001", 1L, "draft-1", "草稿1", "SPU-001", "苹果", 100L));
        draftRepository.seed(draft("corp-001", 2L, "draft-2", "草稿2", "SPU-002", "香蕉", 300L));
        draftRepository.seed(draft("corp-001", 3L, "draft-3", "草稿3", "SPU-003", "梨子", 200L));
        draftRepository.seed(draft("corp-002", 4L, "draft-4", "草稿4", "SPU-004", "其他", 400L));

        ProductDraftListDTO dto = new ProductDraftListDTO();
        dto.setCorpid("corp-001");
        dto.setKeyword("香蕉");
        List<ProductDraftListItemVO> drafts = service.draftList(dto);

        assertEquals(1, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
    }

    @Test
    void should_trim_old_drafts_and_remove_draft_after_submit() {
        InMemoryProductDraftRepository draftRepository = new InMemoryProductDraftRepository();
        ProductSaveAppServiceImpl saveAppService = new ProductSaveAppServiceImpl(new InMemoryProductSpuRepository(), new InMemoryProductSkuRepository(), draftRepository);
        ProductAdminAppServiceImpl service = ProductAdminAppServiceImpl.forTesting(null, null, saveAppService, new ProductDraftAppServiceImpl(draftRepository));

        for (long i = 1; i <= 11; i++) {
            service.saveDraft(saveDto("SPU-" + i, "商品" + i, "草稿" + i, i));
        }
        List<ProductSaveDraftPojo> drafts = draftRepository.listDrafts("corp-001", 20);
        assertEquals(10, drafts.size());
        assertNull(draftRepository.loadDraft("corp-001", drafts.stream().map(ProductSaveDraftPojo::getDraftId).min(Long::compareTo).orElseThrow() - 1));

        ProductSaveDraftPojo latest = drafts.get(0);
        ProductSubmitSaveDTO submitDTO = submitDto(latest.getDraftCode(), latest.getMain().getSpuCode(), latest.getMain().getSpuName());
        BaseVO result = service.saveAndSubmit(submitDTO);

        assertEquals(1, result.getOk());
        assertNull(draftRepository.listDrafts("corp-001", 20).stream().filter(item -> latest.getDraftCode().equals(item.getDraftCode())).findFirst().orElse(null));
    }

    private ProductDraftSaveDTO saveDto(String spuCode, String spuName, String draftName, Long updatedTime) {
        ProductDraftSaveDTO dto = new ProductDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main(spuCode, spuName));
        dto.setSkus(List.of(sku("SKU-001", "默认SKU", "DEFAULT")));
        ProductDraftMetaDTO draftMeta = new ProductDraftMetaDTO();
        draftMeta.setDraftName(draftName);
        draftMeta.setUpdatedTime(updatedTime);
        dto.setDraftMeta(draftMeta);
        return dto;
    }

    private ProductSubmitSaveDTO submitDto(String draftCode, String spuCode, String spuName) {
        ProductSubmitSaveDTO dto = new ProductSubmitSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main(spuCode, spuName));
        dto.setSkus(List.of(sku("SKU-001", "默认SKU", "DEFAULT")));
        ProductDraftMetaDTO draftMeta = new ProductDraftMetaDTO();
        draftMeta.setDraftCode(draftCode);
        dto.setDraftMeta(draftMeta);
        return dto;
    }

    private ProductSaveDraftPojo draft(String corpid, Long draftId, String draftCode, String draftName, String spuCode, String spuName, Long updatedTime) {
        ProductSaveDraftPojo draft = new ProductSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftId(draftId);
        draft.setDraftCode(draftCode);
        draft.setDraftName(draftName);
        draft.setUpdatedTime(updatedTime);
        draft.setMain(main(spuCode, spuName));
        draft.setSkus(List.of(sku("SKU-" + draftId, "默认SKU" + draftId, "DEFAULT-" + draftId)));
        return draft;
    }

    private ProductMainDTO main(String spuCode, String spuName) {
        ProductMainDTO main = new ProductMainDTO();
        main.setSpuCode(spuCode);
        main.setSpuName(spuName);
        main.setEnableSpec(0);
        main.setProductType("NORMAL");
        main.setSpuEnableStatus(1);
        return main;
    }

    private ProductSkuItemDTO sku(String skuCode, String skuName, String specSignature) {
        ProductSkuItemDTO sku = new ProductSkuItemDTO();
        sku.setSkuCode(skuCode);
        sku.setSkuName(skuName);
        sku.setSpecSignature(specSignature);
        sku.setSpecSnapshot(specSignature);
        sku.setSkuEnableStatus(1);
        sku.setListingStatus(1);
        return sku;
    }
}
