package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftSaveVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveContextPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;

import java.util.List;

public class PurchaseOrderDraftAppServiceImpl implements PurchaseOrderDraftAppService {

    private final PurchaseOrderDraftRepository purchaseOrderDraftRepository;
    private final PurchaseOrderSaveProtocolValidator protocolValidator;
    private final PurchaseOrderSaveCommonValidator commonValidator;

    public PurchaseOrderDraftAppServiceImpl(PurchaseOrderDraftRepository purchaseOrderDraftRepository) {
        this.purchaseOrderDraftRepository = purchaseOrderDraftRepository;
        this.protocolValidator = new PurchaseOrderSaveProtocolValidator();
        this.commonValidator = new PurchaseOrderSaveCommonValidator();
    }

    @Override
    public PurchaseOrderDraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto) {
        PurchaseOrderSaveContextPojo context = PurchaseOrderAdminAssembler.toDraftContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        PurchaseOrderSaveDraftPojo draft = PurchaseOrderAdminAssembler.toDraftPojo(dto);
        String draftCode = purchaseOrderDraftRepository.saveDraft(draft);
        if (dto.getDraftMeta() != null) {
            dto.getDraftMeta().setDraftCode(draftCode);
        }
        PurchaseOrderDraftSaveVO vo = new PurchaseOrderDraftSaveVO();
        vo.setDraftCode(draftCode);
        return vo;
    }

    @Override
    public List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto) {
        if (purchaseOrderDraftRepository == null) {
            return List.of();
        }
        return purchaseOrderDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .map(PurchaseOrderAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto) {
        if (purchaseOrderDraftRepository == null) {
            return new PurchaseOrderDraftDetailVO();
        }
        return PurchaseOrderAdminAssembler.toDraftDetailVO(
            purchaseOrderDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
        );
    }
}
