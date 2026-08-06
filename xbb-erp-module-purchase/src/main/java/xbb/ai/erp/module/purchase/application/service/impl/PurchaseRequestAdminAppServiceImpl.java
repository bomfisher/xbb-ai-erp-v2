package xbb.ai.erp.module.purchase.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.port.PurchaseRequestDraftRepository;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestAdminAppService;
import xbb.ai.erp.module.purchase.application.service.delete.PurchaseRequestDeleteAppService;
import xbb.ai.erp.module.purchase.application.service.delete.PurchaseRequestDeleteAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseRequestDraftAppService;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseRequestDraftAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseRequestQueryAppService;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseRequestQueryAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseRequestSaveAppService;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseRequestSaveAppServiceImpl;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.List;

@Service
public class PurchaseRequestAdminAppServiceImpl implements PurchaseRequestAdminAppService {

    private final PurchaseRequestQueryAppService queryAppService;
    private final PurchaseRequestDraftAppService draftAppService;
    private final PurchaseRequestSaveAppService saveAppService;
    private final PurchaseRequestDeleteAppService deleteAppService;

    @Autowired
    public PurchaseRequestAdminAppServiceImpl(
        PurchaseRequestRepository purchaseRequestRepository,
        PurchaseRequestItemRepository purchaseRequestItemRepository,
        PurchaseRequestDraftRepository purchaseRequestDraftRepository
    ) {
        this.queryAppService = new PurchaseRequestQueryAppServiceImpl(purchaseRequestRepository, purchaseRequestItemRepository);
        this.draftAppService = new PurchaseRequestDraftAppServiceImpl(purchaseRequestDraftRepository);
        this.saveAppService = new PurchaseRequestSaveAppServiceImpl(
            purchaseRequestRepository,
            purchaseRequestItemRepository,
            purchaseRequestDraftRepository
        );
        this.deleteAppService = new PurchaseRequestDeleteAppServiceImpl(
            purchaseRequestRepository,
            purchaseRequestItemRepository
        );
    }

    public PurchaseRequestAdminAppServiceImpl(
        PurchaseRequestRepository purchaseRequestRepository,
        PurchaseRequestItemRepository purchaseRequestItemRepository
    ) {
        this(purchaseRequestRepository, purchaseRequestItemRepository, null);
    }

    public PurchaseRequestAdminAppServiceImpl(PurchaseRequestRepository purchaseRequestRepository) {
        this(purchaseRequestRepository, null, null);
    }

    @Override
    public ListBaseVO<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto) {
        return queryAppService.list(dto);
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> addItem(BaseDTO dto) {
        return queryAppService.addItem(dto);
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryAppService.updateItem(dto);
    }

    @Override
    public PurchaseRequestDraftSaveVO saveDraft(PurchaseRequestDraftSaveDTO dto) {
        return draftAppService.saveDraft(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseVO saveAndSubmit(PurchaseRequestSubmitSaveDTO dto) {
        return saveAppService.saveAndSubmit(dto);
    }

    @Override
    public List<PurchaseRequestDraftListItemVO> draftList(PurchaseRequestDraftListDTO dto) {
        return draftAppService.draftList(dto);
    }

    @Override
    public PurchaseRequestDraftDetailVO loadDraft(PurchaseRequestDraftLoadDTO dto) {
        return draftAppService.loadDraft(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PurchaseRequestSaveDTO dto) {
        return saveAppService.save(dto);
    }

    @Override
    public PurchaseRequestDetailVO detail(IdBaseDTO dto) {
        return queryAppService.detail(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(BatchBaseDTO dto) {
        deleteAppService.delete(dto);
    }
}
