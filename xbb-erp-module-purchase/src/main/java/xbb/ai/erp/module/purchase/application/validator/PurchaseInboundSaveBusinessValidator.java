package xbb.ai.erp.module.purchase.application.validator;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

@Component
public class PurchaseInboundSaveBusinessValidator {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseInboundSaveBusinessValidator(PurchaseOrderRepository purchaseOrderRepository,
                                                PurchaseOrderItemRepository purchaseOrderItemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    public void validateForSubmit(PurchaseInboundSaveDTO dto) {
        Long purchaseOrderId = dto.getMain().getPurchaseOrderId();
        if (purchaseOrderId == null) {
            throw new BizException("采购入库必须关联采购订单");
        }
        PurchaseOrder order = purchaseOrderRepository.findById(dto.getCorpid(), purchaseOrderId);
        if (order == null) {
            throw new BizException("采购订单不存在或不属于当前租户");
        }
        if (dto.getMain().getSupplierId() == null || !dto.getMain().getSupplierId().equals(order.getSupplierId())) {
            throw new BizException("入库供应商必须与采购订单一致");
        }
        Set<Long> orderItemIds = new HashSet<>();
        for (PurchaseInboundItemDTO inboundItem : dto.getItems()) {
            if (!orderItemIds.add(inboundItem.getPurchaseOrderItemId())) {
                throw new BizException("同一采购订单行不能重复入库");
            }
            PurchaseOrderItem orderItem = purchaseOrderItemRepository.findById(dto.getCorpid(), inboundItem.getPurchaseOrderItemId());
            if (orderItem == null || !purchaseOrderId.equals(orderItem.getPurchaseOrderId())) {
                throw new BizException("入库产品不属于所选采购订单");
            }
            BigDecimal inboundQty = orderItem.getInboundQty() == null ? BigDecimal.ZERO : orderItem.getInboundQty();
            BigDecimal pendingQty = orderItem.getQty().subtract(inboundQty);
            if (pendingQty.compareTo(BigDecimal.ZERO) <= 0 || inboundItem.getQty().compareTo(pendingQty) > 0) {
                throw new BizException("入库数量超过采购订单待入库数量");
            }
            if (!orderItem.getSkuId().equals(inboundItem.getSkuId()) || orderItem.getUnitPrice().compareTo(inboundItem.getUnitPrice()) != 0) {
                throw new BizException("入库产品或采购单价与采购订单不一致");
            }
        }
    }
}
