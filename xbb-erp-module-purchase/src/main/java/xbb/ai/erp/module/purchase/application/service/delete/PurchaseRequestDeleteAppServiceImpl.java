package xbb.ai.erp.module.purchase.application.service.delete;

import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.Map;

public class PurchaseRequestDeleteAppServiceImpl implements PurchaseRequestDeleteAppService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    public PurchaseRequestDeleteAppServiceImpl(
        PurchaseRequestRepository purchaseRequestRepository,
        PurchaseRequestItemRepository purchaseRequestItemRepository
    ) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestItemRepository = purchaseRequestItemRepository;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        purchaseRequestRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        if (purchaseRequestItemRepository == null) {
            return;
        }
        purchaseRequestItemRepository.removeBatchByIds(dto.getCorpid(), loadItemIds(dto));
    }

    private java.util.List<Long> loadItemIds(BatchBaseDTO dto) {
        return dto.getIdList().stream()
            .flatMap(requestId -> {
                Map<String, Object> conditionMap = new java.util.HashMap<>();
                conditionMap.put("corpid", dto.getCorpid());
                conditionMap.put("requestId", requestId);
                return purchaseRequestItemRepository.findByCondition(conditionMap).stream().map(xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem::getId);
            })
            .filter(java.util.Objects::nonNull)
            .toList();
    }
}
