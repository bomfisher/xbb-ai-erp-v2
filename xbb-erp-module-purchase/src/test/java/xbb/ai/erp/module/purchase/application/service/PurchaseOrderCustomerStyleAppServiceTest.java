package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftSaveVO;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.service.impl.PurchaseOrderAdminAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.application.service.support.InMemoryPurchaseOrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseOrderCustomerStyleAppServiceTest {

    @Test
    void should_use_list_base_dto_for_purchase_order_list() {
        assertEquals("ListBaseDTO", PurchaseOrderListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_save_and_load_purchase_order_draft_through_repository() {
        PurchaseOrderDraftRepository draftRepository = new InMemoryPurchaseOrderDraftRepository();
        PurchaseOrderAdminAppService service = new PurchaseOrderAdminAppServiceImpl(
            new InMemoryPurchaseOrderRepository(),
            new InMemoryPurchaseOrderItemRepository(),
            draftRepository
        );

        PurchaseOrderDraftSaveDTO draftSaveDTO = new PurchaseOrderDraftSaveDTO();
        draftSaveDTO.setCorpid("corp-001");
        draftSaveDTO.setMain(buildMain(null, "PO-DRAFT-001", 20L));
        draftSaveDTO.getItems().add(buildItem(null, 1, 1000L));
        draftSaveDTO.getDraftMeta().setDraftTitle("采购订单草稿");
        draftSaveDTO.getDraftMeta().setUpdatedTime(123L);

        PurchaseOrderDraftSaveVO saveVO = service.saveDraft(draftSaveDTO);
        List<PurchaseOrderDraftListItemVO> draftList = service.draftList(new BaseDTOWithCorpid("corp-001"));
        PurchaseOrderDraftLoadDTO loadDTO = new PurchaseOrderDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(saveVO.getDraftCode());
        PurchaseOrderDraftDetailVO detailVO = service.loadDraft(loadDTO);

        assertNotNull(saveVO.getDraftCode());
        assertEquals(1, draftList.size());
        assertEquals("采购订单草稿", draftList.get(0).getDraftTitle());
        assertEquals("PO-DRAFT-001", draftList.get(0).getOrderNo());
        assertEquals("PO-DRAFT-001", detailVO.getMain().getOrderNo());
        assertEquals(1, detailVO.getItems().size());
        assertEquals(1000L, detailVO.getItems().get(0).getSkuId());
        assertEquals(saveVO.getDraftCode(), detailVO.getDraftMeta().getDraftCode());
    }

    @Test
    void should_remove_draft_after_save_and_submit() {
        InMemoryPurchaseOrderDraftRepository draftRepository = new InMemoryPurchaseOrderDraftRepository();
        InMemoryPurchaseOrderRepository orderRepository = new InMemoryPurchaseOrderRepository();
        InMemoryPurchaseOrderItemRepository itemRepository = new InMemoryPurchaseOrderItemRepository();
        PurchaseOrderAdminAppService service = new PurchaseOrderAdminAppServiceImpl(
            orderRepository,
            itemRepository,
            draftRepository
        );

        PurchaseOrderDraftSaveDTO draftSaveDTO = new PurchaseOrderDraftSaveDTO();
        draftSaveDTO.setCorpid("corp-001");
        draftSaveDTO.setUserId("user-001");
        draftSaveDTO.setMain(buildMain(null, "PO-SUBMIT-001", 20L));
        draftSaveDTO.getItems().add(buildItem(null, 1, 1001L));
        draftSaveDTO.getDraftMeta().setDraftTitle("待提交草稿");
        String draftCode = service.saveDraft(draftSaveDTO).getDraftCode();

        PurchaseOrderSubmitSaveDTO submitDTO = new PurchaseOrderSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setUserId("user-001");
        submitDTO.setMain(buildMain(null, "PO-SUBMIT-001", 20L));
        submitDTO.getItems().add(buildItem(null, 1, 1001L));
        submitDTO.getDraftMeta().setDraftCode(draftCode);
        BaseVO result = service.saveAndSubmit(submitDTO);

        assertInstanceOf(BaseVO.class, result);
        assertEquals(1, orderRepository.all().size());
        assertEquals(1, itemRepository.all().size());
        assertEquals(0, draftRepository.listDrafts("corp-001", 10).size());
    }

    private PurchaseOrderMainDTO buildMain(Long id, String orderNo, Long vendorId) {
        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setId(id);
        main.setPurchaseOrgId(10L);
        main.setOrderNo(orderNo);
        main.setVendorId(vendorId);
        main.setVendorNameSnapshot("供应商" + vendorId);
        return main;
    }

    private PurchaseOrderItemMainDTO buildItem(Long id, Integer lineNo, Long skuId) {
        PurchaseOrderItemMainDTO item = new PurchaseOrderItemMainDTO();
        item.setId(id);
        item.setLineNo(lineNo);
        item.setSkuId(skuId);
        item.setSkuCodeSnapshot("SKU-" + skuId);
        item.setSkuNameSnapshot("商品" + skuId);
        item.setOrderQty(BigDecimal.ONE);
        return item;
    }

    private static class BaseDTOWithCorpid extends xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO {
        private BaseDTOWithCorpid(String corpid) {
            setCorpid(corpid);
        }
    }

    private static class InMemoryPurchaseOrderDraftRepository implements PurchaseOrderDraftRepository {
        private final List<PurchaseOrderSaveDraftPojo> drafts = new ArrayList<>();
        private long sequence = 1L;

        @Override
        public String saveDraft(PurchaseOrderSaveDraftPojo draft) {
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
            drafts.sort(Comparator.comparing(PurchaseOrderSaveDraftPojo::getUpdatedTime, Comparator.nullsLast(Long::compareTo)).reversed());
            return draftCode;
        }

        @Override
        public List<PurchaseOrderSaveDraftPojo> listDrafts(String corpid, int limit) {
            return drafts.stream()
                .filter(item -> corpid.equals(item.getCorpid()))
                .limit(limit)
                .toList();
        }

        @Override
        public PurchaseOrderSaveDraftPojo loadDraft(String corpid, String draftCode) {
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
