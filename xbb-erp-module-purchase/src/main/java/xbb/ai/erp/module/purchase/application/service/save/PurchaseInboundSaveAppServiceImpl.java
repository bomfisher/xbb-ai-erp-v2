package xbb.ai.erp.module.purchase.application.service.save;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;

@Service
public class PurchaseInboundSaveAppServiceImpl {

    private final PurchaseInboundRepository purchaseInboundRepository;

    public PurchaseInboundSaveAppServiceImpl(PurchaseInboundRepository purchaseInboundRepository) {
        this.purchaseInboundRepository = purchaseInboundRepository;
    }

    public Long save(PurchaseInboundSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseInboundValidator.validateSave(dto);
        PurchaseInbound entity = PurchaseInboundAdminAssembler.toPurchaseInbound(dto);
        if (entity.getId() == null) {
            purchaseInboundRepository.insert(entity);
        } else {
            purchaseInboundRepository.update(entity);
        }
        return entity.getId();
    }
}
