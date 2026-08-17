package xbb.ai.erp.module.sales.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSubmitSaveDTO;
import xbb.ai.erp.module.sales.application.assembler.SalesOutboundAdminAssembler;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundValidator;
import xbb.ai.erp.module.sales.application.port.SalesOutboundDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveProtocolValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveBusinessValidator;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundItemDTO;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.OutboundCommand;
import xbb.ai.erp.module.inventory.contract.OutboundLine;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOutboundSaveAppServiceImpl {

    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final InventoryCommandApi inventoryCommandApi;

    private final SalesOutboundDraftRepository draftRepository;
    private final SalesOutboundSaveProtocolValidator protocolValidator;
    private final SalesOutboundSaveCommonValidator commonValidator;
    private final SalesOutboundSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(SalesOutboundSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        Long outboundId = save(dto);
        List<SalesOutboundItem> items = dto.getItems().stream().map(item -> toItem(item, dto, outboundId)).toList();
        if (items.isEmpty()) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库明细不能为空");
        }
        salesOutboundItemRepository.insertBatch(items);
        inventoryCommandApi.postReservedOutbound(new OutboundCommand(dto.getCorpid(), "SALES_OUTBOUND",
            dto.getMain().getSalesOrderId(), "SALES_ORDER", items.stream()
                .map(item -> new OutboundLine(dto.getMain().getWarehouseId(), item.getSkuId(), item.getQty(), item.getSalesOrderItemId())).toList(),
            dto.getUserId(), LocalDateTime.now(), "sales-outbound-" + outboundId));
        for (SalesOutboundItem item : items) {
            SalesOrderItem orderItem = salesOrderItemRepository.findById(dto.getCorpid(), item.getSalesOrderItemId());
            if (orderItem == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("销售订单行不存在");
            }
            java.math.BigDecimal delivered = orderItem.getDeliveredQty() == null ? BigDecimal.ZERO : orderItem.getDeliveredQty();
            delivered = delivered.add(item.getQty());
            if (delivered.compareTo(orderItem.getQty()) > 0) {
                throw new xbb.ai.erp.base.common.exception.BizException("出库数量超过订单待出库数量");
            }
            orderItem.setDeliveredQty(delivered);
            orderItem.setOutboundStatus(delivered.compareTo(orderItem.getQty()) == 0 ? 2 : 1);
            orderItem.setModifyId(dto.getUserId());
            salesOrderItemRepository.update(orderItem);
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(SalesOutboundSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SalesOutboundValidator.validateSave(dto);
        SalesOutbound entity = SalesOutboundAdminAssembler.toSalesOutbound(dto);
        if (entity.getId() == null) {
            return salesOutboundRepository.insert(entity);
        }
        salesOutboundRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            salesOutboundRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private static SalesOutboundItem toItem(SalesOutboundItemDTO dto, SalesOutboundSaveDTO request, Long outboundId) {
        if (dto.getSalesOrderItemId() == null || dto.getSkuId() == null || dto.getQty() == null || dto.getQty().signum() <= 0
            || dto.getUnitPrice() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库明细数据不完整");
        }
        SalesOutboundItem item = new SalesOutboundItem();
        item.setCorpid(request.getCorpid()); item.setSalesOutboundId(outboundId); item.setSalesOrderItemId(dto.getSalesOrderItemId());
        item.setSkuId(dto.getSkuId()); item.setSkuName(dto.getSkuName()); item.setUnitName(dto.getUnitName()); item.setQty(dto.getQty()); item.setUnitPrice(dto.getUnitPrice());
        item.setAmount(dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP)); item.setCostUnit(BigDecimal.ZERO); item.setCostAmount(BigDecimal.ZERO);
        item.setOutboundStatus(2); item.setCreatorId(request.getUserId()); item.setModifyId(request.getUserId()); return item;
    }
}
