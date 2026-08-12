package xbb.ai.erp.module.purchase.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderValidator;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveBusinessValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

@Service
@RequiredArgsConstructor
public class PurchaseOrderSaveAppServiceImpl {

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderDraftRepository draftRepository;
    private final PurchaseOrderSaveProtocolValidator protocolValidator;
    private final PurchaseOrderSaveCommonValidator commonValidator;
    private final PurchaseOrderSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(PurchaseOrderSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseOrderValidator.validateSave(dto);
        PurchaseOrder entity = PurchaseOrderAdminAssembler.toPurchaseOrder(dto);
        long now = System.currentTimeMillis();
        entity.setModifyId(dto.getUserId());
        entity.setUpdateTime(now);
        if (entity.getId() == null) {
            entity.setCreatorId(dto.getUserId());
            entity.setAddTime(now);
            entity.setDel(0);
            if (entity.getSupplierName() == null || entity.getSupplierName().isBlank()) entity.setSupplierName("MOCK");
            if (entity.getTotalAmount() == null) entity.setTotalAmount(BigDecimal.ZERO);
            if (entity.getStatus() == null || entity.getStatus().isBlank()) entity.setStatus("1");
            return purchaseOrderRepository.insert(entity);
        }
        purchaseOrderRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            purchaseOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
