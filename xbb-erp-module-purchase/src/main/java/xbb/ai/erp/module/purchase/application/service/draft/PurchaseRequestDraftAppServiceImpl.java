package xbb.ai.erp.module.purchase.application.service.draft;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseRequestAdminAssembler;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveContextPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseRequestDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseRequestSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseRequestSaveProtocolValidator;

import java.util.List;

public class PurchaseRequestDraftAppServiceImpl implements PurchaseRequestDraftAppService {

    private final PurchaseRequestDraftRepository purchaseRequestDraftRepository;
    private final PurchaseRequestSaveProtocolValidator protocolValidator;
    private final PurchaseRequestSaveCommonValidator commonValidator;

    public PurchaseRequestDraftAppServiceImpl(PurchaseRequestDraftRepository purchaseRequestDraftRepository) {
        this.purchaseRequestDraftRepository = purchaseRequestDraftRepository;
        this.protocolValidator = new PurchaseRequestSaveProtocolValidator();
        this.commonValidator = new PurchaseRequestSaveCommonValidator();
    }

    @Override
    public PurchaseRequestDraftSaveVO saveDraft(PurchaseRequestDraftSaveDTO dto) {
        PurchaseRequestSaveContextPojo context = PurchaseRequestAdminAssembler.toDraftContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        PurchaseRequestSaveDraftPojo draft = PurchaseRequestAdminAssembler.toDraftPojo(dto);
        String draftCode = purchaseRequestDraftRepository.saveDraft(draft);
        if (dto.getDraftMeta() != null) {
            dto.getDraftMeta().setDraftCode(draftCode);
        }
        PurchaseRequestDraftSaveVO vo = new PurchaseRequestDraftSaveVO();
        vo.setDraftCode(draftCode);
        return vo;
    }

    @Override
    public List<PurchaseRequestDraftListItemVO> draftList(PurchaseRequestDraftListDTO dto) {
        if (purchaseRequestDraftRepository == null) {
            return List.of();
        }
        return purchaseRequestDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .map(PurchaseRequestAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public PurchaseRequestDraftDetailVO loadDraft(PurchaseRequestDraftLoadDTO dto) {
        if (purchaseRequestDraftRepository == null) {
            return new PurchaseRequestDraftDetailVO();
        }
        return PurchaseRequestAdminAssembler.toDraftDetailVO(
            purchaseRequestDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
        );
    }
}
