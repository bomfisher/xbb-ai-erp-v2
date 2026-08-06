package xbb.ai.erp.module.purchase.application.service.delete;

import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.Map;

public class PurchaseOrderDeleteAppServiceImpl implements PurchaseOrderDeleteAppService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderDeleteAppServiceImpl(
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        purchaseOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        if (purchaseOrderItemRepository == null) {
            return;
        }
        purchaseOrderItemRepository.removeBatchByIds(dto.getCorpid(), loadItemIds(dto));
    }

    private java.util.List<Long> loadItemIds(BatchBaseDTO dto) {
        return dto.getIdList().stream()
            .flatMap(orderId -> {
                Map<String, Object> conditionMap = new java.util.HashMap<>();
                conditionMap.put("corpid", dto.getCorpid());
                conditionMap.put("orderId", orderId);
                return purchaseOrderItemRepository.findByCondition(conditionMap).stream().map(xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem::getId);
            })
            .filter(java.util.Objects::nonNull)
            .toList();
    }
}
