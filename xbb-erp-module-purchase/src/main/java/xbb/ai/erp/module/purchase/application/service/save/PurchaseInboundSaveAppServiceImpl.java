package xbb.ai.erp.module.purchase.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundValidator;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveProtocolValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveBusinessValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;

@Service
@RequiredArgsConstructor
public class PurchaseInboundSaveAppServiceImpl {

    private final PurchaseInboundRepository purchaseInboundRepository;

    private final PurchaseInboundDraftRepository draftRepository;
    private final PurchaseInboundSaveProtocolValidator protocolValidator;
    private final PurchaseInboundSaveCommonValidator commonValidator;
    private final PurchaseInboundSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(PurchaseInboundSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(PurchaseInboundSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseInboundValidator.validateSave(dto);
        PurchaseInbound entity = PurchaseInboundAdminAssembler.toPurchaseInbound(dto);
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
            return purchaseInboundRepository.insert(entity);
        }
        purchaseInboundRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            purchaseInboundRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
