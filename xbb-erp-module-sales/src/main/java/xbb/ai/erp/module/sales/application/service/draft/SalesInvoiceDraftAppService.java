package xbb.ai.erp.module.sales.application.service.draft;

import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface SalesInvoiceDraftAppService {
    DraftSaveVO saveDraft(SalesInvoiceDraftSaveDTO dto);

    List<SalesInvoiceDraftListItemVO> draftList(SalesInvoiceDraftListDTO dto);

    SalesInvoiceDraftDetailVO loadDraft(SalesInvoiceDraftLoadDTO dto);
}
