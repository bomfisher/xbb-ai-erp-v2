package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseRequestDraftListDTOStructureTest {

    @Test
    void should_use_list_base_dto_for_purchase_request_draft_list() {
        assertEquals(ListBaseDTO.class, PurchaseRequestDraftListDTO.class.getSuperclass());
    }
}
