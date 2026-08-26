package xbb.ai.erp.module.settlement.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDraftListItemVO;
import xbb.ai.erp.module.settlement.application.pojo.PaymentSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.PaymentDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.PaymentSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.PaymentSaveProtocolValidator;
import xbb.ai.erp.module.settlement.admin.PaymentTypeEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentDraftAppServiceImpl implements PaymentDraftAppService {

    private final PaymentDraftRepository repository;
    private final PaymentSaveProtocolValidator protocolValidator;
    private final PaymentSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(PaymentDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        PaymentSaveDraftPojo draft = new PaymentSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setPaymentInfos(dto.getPaymentInfos());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    public DraftSaveVO saveDraftByPaymentType(PaymentDraftSaveDTO dto, PaymentTypeEnum paymentType) {
        if (dto.getMain() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款单主信息不能为空");
        }
        dto.getMain().setPaymentType(paymentType.getCode());
        return saveDraft(dto);
    }

    @Override
    public List<PaymentDraftListItemVO> draftList(PaymentDraftListDTO dto) {
        return draftListByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public List<PaymentDraftListItemVO> draftListByPaymentType(PaymentDraftListDTO dto, PaymentTypeEnum paymentType) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            if (draft.getMain() == null || !paymentType.getCode().equals(draft.getMain().getPaymentType())) {
                return null;
            }
            PaymentDraftListItemVO vo = new PaymentDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).filter(java.util.Objects::nonNull).toList();
    }

    @Override
    public PaymentDraftDetailVO loadDraft(PaymentDraftLoadDTO dto) {
        return loadDraftByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    @Override
    public PaymentDraftDetailVO loadDraftByPaymentType(PaymentDraftLoadDTO dto, PaymentTypeEnum paymentType) {
        PaymentSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        if (draft != null && (draft.getMain() == null || !paymentType.getCode().equals(draft.getMain().getPaymentType()))) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款草稿不存在或不属于当前业务");
        }
        PaymentDraftDetailVO vo = new PaymentDraftDetailVO();
        if (draft != null) {
            vo.setDraftCode(draft.getDraftCode());
            vo.setMain(draft.getMain());
            vo.setPaymentInfos(draft.getPaymentInfos());
        }
        return vo;
    }
}
