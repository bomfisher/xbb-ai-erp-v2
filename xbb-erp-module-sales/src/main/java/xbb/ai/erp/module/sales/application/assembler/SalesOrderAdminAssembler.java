package xbb.ai.erp.module.sales.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import java.util.List;
import xbb.ai.erp.base.common.enums.DocumentStatusEnum;

public final class SalesOrderAdminAssembler {

    private SalesOrderAdminAssembler() {
    }

    public static SalesOrderSaveItemVO buildEmptySaveItemVO() {
        SalesOrderSaveItemVO vo = new SalesOrderSaveItemVO();
        SalesOrderMainDTO main = new SalesOrderMainDTO();
        main.setStatus(DocumentStatusEnum.OPEN.getCode());
        vo.setMain(main);
        return vo;
    }

    public static SalesOrder toSalesOrder(SalesOrderSaveDTO dto) {
        SalesOrder salesOrder = new SalesOrder();
        SalesOrderMainDTO main = dto.getMain();
        if (main != null) {
            salesOrder.setId(main.getId());
            salesOrder.setCorpid(main.getCorpid());
            salesOrder.setOrderNo(main.getOrderNo());
            salesOrder.setCustomerId(main.getCustomerId());
            salesOrder.setWarehouseId(main.getWarehouseId());
            salesOrder.setOrderDate(main.getOrderDate());
            salesOrder.setDeliveryDate(main.getDeliveryDate());
            salesOrder.setTotalAmount(main.getTotalAmount());
            salesOrder.setStatus(main.getStatus());
            salesOrder.setRemark(main.getRemark());
            salesOrder.setAuditStatus(main.getAuditStatus());
            salesOrder.setOutboundStatus(main.getOutboundStatus());
            salesOrder.setReceiptStatus(main.getReceiptStatus());
            salesOrder.setInvoiceStatus(main.getInvoiceStatus());
            salesOrder.setCreatorId(main.getCreatorId());
            salesOrder.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                salesOrder.setCreatorId(dto.getUserId());
            }
            salesOrder.setModifyId(dto.getUserId());
        }
        salesOrder.setCorpid(dto.getCorpid());
        return salesOrder;
    }

    public static SalesOrderListItemVO toListItemVO(SalesOrder salesOrder) {
        SalesOrderListItemVO vo = new SalesOrderListItemVO();
        vo.setId(Objects.isNull(salesOrder.getId()) ? "" : Objects.toString(salesOrder.getId()));
        vo.setOrderNo(salesOrder.getOrderNo());
        vo.setCustomerId(Objects.isNull(salesOrder.getCustomerId()) ? "" : Objects.toString(salesOrder.getCustomerId()));
        vo.setOrderDate(Objects.isNull(salesOrder.getOrderDate()) ? "" : Objects.toString(salesOrder.getOrderDate()));
        vo.setDeliveryDate(Objects.isNull(salesOrder.getDeliveryDate()) ? "" : Objects.toString(salesOrder.getDeliveryDate()));
        vo.setTotalAmount(Objects.isNull(salesOrder.getTotalAmount()) ? "" : Objects.toString(salesOrder.getTotalAmount()));
        vo.setStatus(Objects.isNull(salesOrder.getStatus()) ? "" : Objects.toString(salesOrder.getStatus()));
        vo.setRemark(salesOrder.getRemark());
        vo.setAuditStatus(Objects.isNull(salesOrder.getAuditStatus()) ? "" : Objects.toString(salesOrder.getAuditStatus()));
        vo.setOutboundStatus(Objects.isNull(salesOrder.getOutboundStatus()) ? "" : Objects.toString(salesOrder.getOutboundStatus()));
        vo.setReceiptStatus(Objects.isNull(salesOrder.getReceiptStatus()) ? "" : Objects.toString(salesOrder.getReceiptStatus()));
        vo.setInvoiceStatus(Objects.isNull(salesOrder.getInvoiceStatus()) ? "" : Objects.toString(salesOrder.getInvoiceStatus()));
        vo.setCreatorId(salesOrder.getCreatorId());
        vo.setModifyId(salesOrder.getModifyId());
        return vo;
    }

    public static SalesOrderSaveItemVO toSaveItemVO(SalesOrder salesOrder) {
        return toSaveItemVO(salesOrder, List.of());
    }

    public static SalesOrderSaveItemVO toSaveItemVO(SalesOrder salesOrder, List<SalesOrderItem> salesOrderItems) {
        SalesOrderSaveItemVO vo = new SalesOrderSaveItemVO();
        if (salesOrder == null) {
            return vo;
        }
        SalesOrderMainDTO main = new SalesOrderMainDTO();
        main.setId(salesOrder.getId());
        main.setCorpid(salesOrder.getCorpid());
        main.setOrderNo(salesOrder.getOrderNo());
        main.setCustomerId(salesOrder.getCustomerId());
        main.setWarehouseId(salesOrder.getWarehouseId());
        main.setOrderDate(salesOrder.getOrderDate());
        main.setDeliveryDate(salesOrder.getDeliveryDate());
        main.setTotalAmount(salesOrder.getTotalAmount());
        main.setStatus(salesOrder.getStatus());
        main.setRemark(salesOrder.getRemark());
        main.setAuditStatus(salesOrder.getAuditStatus());
        main.setOutboundStatus(salesOrder.getOutboundStatus());
        main.setReceiptStatus(salesOrder.getReceiptStatus());
        main.setInvoiceStatus(salesOrder.getInvoiceStatus());
        main.setCreatorId(salesOrder.getCreatorId());
        main.setModifyId(salesOrder.getModifyId());
        vo.setMain(main);
        vo.setItems(salesOrderItems.stream().map(SalesOrderAdminAssembler::toSalesOrderItemDTO).toList());
        return vo;
    }

    private static SalesOrderItemDTO toSalesOrderItemDTO(SalesOrderItem salesOrderItem) {
        SalesOrderItemDTO dto = new SalesOrderItemDTO();
        dto.setId(salesOrderItem.getId());
        dto.setWarehouseId(salesOrderItem.getWarehouseId());
        dto.setSkuId(salesOrderItem.getSkuId());
        dto.setSkuCode(salesOrderItem.getSkuCode());
        dto.setSkuName(salesOrderItem.getSkuName());
        dto.setSpecification(salesOrderItem.getSpecification());
        dto.setUnitName(salesOrderItem.getUnitName());
        dto.setQty(salesOrderItem.getQty());
        dto.setDeliveredQty(salesOrderItem.getDeliveredQty());
        dto.setUnitPrice(salesOrderItem.getUnitPrice());
        dto.setTaxRate(salesOrderItem.getTaxRate());
        dto.setAmount(salesOrderItem.getAmount());
        return dto;
    }

    public static SalesOrderDetailVO toDetailVO(SalesOrderSaveItemVO saveItemVO) {
        SalesOrderDetailVO detailVO = new SalesOrderDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
