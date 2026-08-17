package xbb.ai.erp.module.sales.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;
import xbb.ai.erp.module.sales.application.assembler.SalesOrderAdminAssembler;
import xbb.ai.erp.module.sales.application.validator.SalesOrderValidator;
import xbb.ai.erp.module.sales.application.port.SalesOrderDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveProtocolValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveBusinessValidator;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.ReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationLine;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SalesOrderSaveAppServiceImpl {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final InventoryCommandApi inventoryCommandApi;

    private final SalesOrderDraftRepository draftRepository;
    private final SalesOrderSaveProtocolValidator protocolValidator;
    private final SalesOrderSaveCommonValidator commonValidator;
    private final SalesOrderSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(SalesOrderSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BizException("销售订单明细不能为空");
        }
        dto.getMain().setTotalAmount(calculateTotalAmount(dto.getItems()));
        Long orderId = save(dto);
        List<SalesOrderItem> items = IntStream.range(0, dto.getItems().size())
            .mapToObj(index -> toItem(dto.getItems().get(index), dto, orderId, index + 1))
            .toList();
        salesOrderItemRepository.insertBatch(items);
        List<ReservationLine> reservationLines = items.stream()
            .filter(item -> item.getWarehouseId() != null)
            .map(item -> new ReservationLine(item.getWarehouseId(), item.getSkuId(), item.getQty(), item.getId()))
            .toList();
        if (!reservationLines.isEmpty()) {
            inventoryCommandApi.reserve(new ReservationCommand(dto.getCorpid(), BusinessCodeEnum.SALES_ORDER.getCode(), orderId,
                BusinessCodeEnum.SALES_ORDER.getCode(), reservationLines, dto.getUserId(), LocalDateTime.now(), "sales-order-" + orderId));
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(SalesOrderSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SalesOrderValidator.validateSave(dto);
        SalesOrder entity = SalesOrderAdminAssembler.toSalesOrder(dto);
        if (entity.getId() == null) {
            entity.setStatus(DocumentStatusEnum.OPEN.getCode());
            return salesOrderRepository.insert(entity);
        }
        salesOrderRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            salesOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private static SalesOrderItem toItem(SalesOrderItemDTO dto, SalesOrderSaveDTO request, Long orderId, int lineNo) {
        if (dto.getSkuId() == null || dto.getQty() == null || dto.getQty().signum() <= 0 || dto.getUnitPrice() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单明细数据不完整");
        }
        SalesOrderItem item = new SalesOrderItem();
        item.setCorpid(request.getCorpid()); item.setSalesOrderId(orderId); item.setLineNo(lineNo); item.setSkuId(dto.getSkuId()); item.setSkuCode(dto.getSkuCode()); item.setSkuName(dto.getSkuName());
        item.setWarehouseId(dto.getWarehouseId());
        item.setSpecification(dto.getSpecification()); item.setUnitName(dto.getUnitName()); item.setQty(dto.getQty()); item.setDeliveredQty(java.math.BigDecimal.ZERO);
        item.setUnitPrice(dto.getUnitPrice()); item.setTaxRate(dto.getTaxRate() == null ? java.math.BigDecimal.ZERO : dto.getTaxRate());
        item.setAmount(dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP)); item.setCreatorId(request.getUserId()); item.setModifyId(request.getUserId());
        return item;
    }

    private static BigDecimal calculateTotalAmount(List<SalesOrderItemDTO> items) {
        return items.stream()
            .filter(item -> item.getQty() != null && item.getUnitPrice() != null)
            .map(item -> item.getQty().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }
}
