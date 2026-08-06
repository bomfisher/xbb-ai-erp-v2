package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;
import xbb.ai.erp.module.purchase.application.port.PurchaseRequestDraftRepository;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseRequestAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseRequestRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseRequestCustomerStyleAppServiceTest {

    @Test
    void should_use_list_base_dto_for_purchase_request_list() {
        assertEquals("ListBaseDTO", PurchaseRequestListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_save_and_load_purchase_request_draft_through_repository() {
        PurchaseRequestDraftRepository draftRepository = new InMemoryPurchaseRequestDraftRepository();
        PurchaseRequestAdminAppService service = new PurchaseRequestAdminAppServiceImpl(
            new InMemoryPurchaseRequestRepository(),
            new InMemoryPurchaseRequestItemRepository(),
            draftRepository
        );

        PurchaseRequestDraftSaveDTO draftSaveDTO = new PurchaseRequestDraftSaveDTO();
        draftSaveDTO.setCorpid("corp-001");
        draftSaveDTO.setMain(buildMain(null, "PR-DRAFT-001", "emp-001"));
        draftSaveDTO.getItems().add(buildItem(null, 1, 1000L));
        draftSaveDTO.getDraftMeta().setDraftTitle("采购申请草稿");
        draftSaveDTO.getDraftMeta().setUpdatedTime(123L);

        PurchaseRequestDraftSaveVO saveVO = service.saveDraft(draftSaveDTO);
        List<PurchaseRequestDraftListItemVO> draftList = service.draftList(new BaseDTOWithCorpid("corp-001"));
        PurchaseRequestDraftLoadDTO loadDTO = new PurchaseRequestDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(saveVO.getDraftCode());
        PurchaseRequestDraftDetailVO detailVO = service.loadDraft(loadDTO);

        assertNotNull(saveVO.getDraftCode());
        assertEquals(1, draftList.size());
        assertEquals("采购申请草稿", draftList.get(0).getDraftTitle());
        assertEquals("PR-DRAFT-001", draftList.get(0).getRequestNo());
        assertEquals("PR-DRAFT-001", detailVO.getMain().getRequestNo());
        assertEquals(1, detailVO.getItems().size());
        assertEquals(1000L, detailVO.getItems().get(0).getSkuId());
        assertEquals(saveVO.getDraftCode(), detailVO.getDraftMeta().getDraftCode());
    }

    @Test
    void should_remove_draft_after_save_and_submit() {
        InMemoryPurchaseRequestDraftRepository draftRepository = new InMemoryPurchaseRequestDraftRepository();
        InMemoryPurchaseRequestRepository requestRepository = new InMemoryPurchaseRequestRepository();
        InMemoryPurchaseRequestItemRepository itemRepository = new InMemoryPurchaseRequestItemRepository();
        PurchaseRequestAdminAppService service = new PurchaseRequestAdminAppServiceImpl(
            requestRepository,
            itemRepository,
            draftRepository
        );

        PurchaseRequestDraftSaveDTO draftSaveDTO = new PurchaseRequestDraftSaveDTO();
        draftSaveDTO.setCorpid("corp-001");
        draftSaveDTO.setUserId("user-001");
        draftSaveDTO.setMain(buildMain(null, "PR-SUBMIT-001", "emp-001"));
        draftSaveDTO.getItems().add(buildItem(null, 1, 1001L));
        draftSaveDTO.getDraftMeta().setDraftTitle("待提交草稿");
        String draftCode = service.saveDraft(draftSaveDTO).getDraftCode();

        PurchaseRequestSubmitSaveDTO submitDTO = new PurchaseRequestSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setUserId("user-001");
        submitDTO.setMain(buildMain(null, "PR-SUBMIT-001", "emp-001"));
        submitDTO.getItems().add(buildItem(null, 1, 1001L));
        submitDTO.getDraftMeta().setDraftCode(draftCode);
        BaseVO result = service.saveAndSubmit(submitDTO);

        assertInstanceOf(BaseVO.class, result);
        assertEquals(1, requestRepository.all().size());
        assertEquals(1, itemRepository.all().size());
        assertEquals(0, draftRepository.listDrafts("corp-001", 10).size());
    }

    private PurchaseRequestMainDTO buildMain(Long id, String requestNo, String applicantId) {
        PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
        main.setId(id);
        main.setPurchaseOrgId(10L);
        main.setRequestNo(requestNo);
        main.setApplicantId(applicantId);
        return main;
    }

    private PurchaseRequestItemMainDTO buildItem(Long id, Integer lineNo, Long skuId) {
        PurchaseRequestItemMainDTO item = new PurchaseRequestItemMainDTO();
        item.setId(id);
        item.setLineNo(lineNo);
        item.setSkuId(skuId);
        item.setSkuCodeSnapshot("SKU-" + skuId);
        item.setSkuNameSnapshot("商品" + skuId);
        item.setRequestQty(java.math.BigDecimal.ONE);
        return item;
    }

    private static class BaseDTOWithCorpid extends xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO {
        private BaseDTOWithCorpid(String corpid) {
            setCorpid(corpid);
        }
    }

    private static class InMemoryPurchaseRequestDraftRepository implements PurchaseRequestDraftRepository {
        private final List<PurchaseRequestSaveDraftPojo> drafts = new ArrayList<>();
        private long sequence = 1L;

        @Override
        public String saveDraft(PurchaseRequestSaveDraftPojo draft) {
            String draftCode = draft.getDraftCode();
            if (draftCode == null || draftCode.isBlank()) {
                draftCode = "draft-" + sequence++;
                draft.setDraftCode(draftCode);
            }
            if (draft.getUpdatedTime() == null) {
                draft.setUpdatedTime(System.currentTimeMillis());
            }
            removeDraft(draft.getCorpid(), draftCode);
            drafts.add(draft);
            drafts.sort(Comparator.comparing(PurchaseRequestSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Long::compareTo)).reversed());
            return draftCode;
        }

        @Override
        public List<PurchaseRequestSaveDraftPojo> listDrafts(String corpid, int limit) {
            return drafts.stream()
                .filter(item -> corpid.equals(item.getCorpid()))
                .limit(limit)
                .toList();
        }

        @Override
        public PurchaseRequestSaveDraftPojo loadDraft(String corpid, String draftCode) {
            return drafts.stream()
                .filter(item -> corpid.equals(item.getCorpid()) && draftCode.equals(item.getDraftCode()))
                .findFirst()
                .orElse(null);
        }

        @Override
        public void removeDraft(String corpid, String draftCode) {
            drafts.removeIf(item -> corpid.equals(item.getCorpid()) && draftCode.equals(item.getDraftCode()));
        }
    }
}
