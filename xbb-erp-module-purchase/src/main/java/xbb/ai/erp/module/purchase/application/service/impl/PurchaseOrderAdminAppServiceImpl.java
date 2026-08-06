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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderAdminAppService;
import xbb.ai.erp.module.purchase.application.service.delete.PurchaseOrderDeleteAppService;
import xbb.ai.erp.module.purchase.application.service.delete.PurchaseOrderDeleteAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseOrderDraftAppService;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseOrderDraftAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseOrderQueryAppService;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseOrderQueryAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseOrderSaveAppService;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseOrderSaveAppServiceImpl;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.List;

@Service
public class PurchaseOrderAdminAppServiceImpl implements PurchaseOrderAdminAppService {

    private final PurchaseOrderQueryAppService queryAppService;
    private final PurchaseOrderDraftAppService draftAppService;
    private final PurchaseOrderSaveAppService saveAppService;
    private final PurchaseOrderDeleteAppService deleteAppService;

    @Autowired
    public PurchaseOrderAdminAppServiceImpl(
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository,
        PurchaseOrderDraftRepository purchaseOrderDraftRepository
    ) {
        this.queryAppService = new PurchaseOrderQueryAppServiceImpl(purchaseOrderRepository, purchaseOrderItemRepository);
        this.draftAppService = new PurchaseOrderDraftAppServiceImpl(purchaseOrderDraftRepository);
        this.saveAppService = new PurchaseOrderSaveAppServiceImpl(
            purchaseOrderRepository,
            purchaseOrderItemRepository,
            purchaseOrderDraftRepository
        );
        this.deleteAppService = new PurchaseOrderDeleteAppServiceImpl(
            purchaseOrderRepository,
            purchaseOrderItemRepository
        );
    }

    public PurchaseOrderAdminAppServiceImpl(
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository
    ) {
        this(purchaseOrderRepository, purchaseOrderItemRepository, null);
    }

    public PurchaseOrderAdminAppServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this(purchaseOrderRepository, null, null);
    }

    @Override
    public ListBaseVO<PurchaseOrderListItemVO> list(PurchaseOrderListDTO dto) {
        return queryAppService.list(dto);
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        return queryAppService.addItem(dto);
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryAppService.updateItem(dto);
    }

    @Override
    public PurchaseOrderDraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto) {
        return draftAppService.saveDraft(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto) {
        return saveAppService.saveAndSubmit(dto);
    }

    @Override
    public List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto) {
        return draftAppService.draftList(dto);
    }

    @Override
    public PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto) {
        return draftAppService.loadDraft(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PurchaseOrderSaveDTO dto) {
        return saveAppService.save(dto);
    }

    @Override
    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        return queryAppService.detail(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(BatchBaseDTO dto) {
        deleteAppService.delete(dto);
    }
}
