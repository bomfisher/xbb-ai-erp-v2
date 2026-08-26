package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface PurchaseInvoiceDraftAppService {
    DraftSaveVO saveDraft(PurchaseInvoiceDraftSaveDTO dto);

    List<PurchaseInvoiceDraftListItemVO> draftList(PurchaseInvoiceDraftListDTO dto);

    PurchaseInvoiceDraftDetailVO loadDraft(PurchaseInvoiceDraftLoadDTO dto);
}
