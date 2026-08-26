package xbb.ai.erp.module.settlement.application.service.draft;

import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.PaymentTypeEnum;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface PaymentDraftAppService {
    DraftSaveVO saveDraft(PaymentDraftSaveDTO dto);

    DraftSaveVO saveDraftByPaymentType(PaymentDraftSaveDTO dto, PaymentTypeEnum paymentType);

    List<PaymentDraftListItemVO> draftList(PaymentDraftListDTO dto);

    List<PaymentDraftListItemVO> draftListByPaymentType(PaymentDraftListDTO dto, PaymentTypeEnum paymentType);

    PaymentDraftDetailVO loadDraft(PaymentDraftLoadDTO dto);

    PaymentDraftDetailVO loadDraftByPaymentType(PaymentDraftLoadDTO dto, PaymentTypeEnum paymentType);
}
