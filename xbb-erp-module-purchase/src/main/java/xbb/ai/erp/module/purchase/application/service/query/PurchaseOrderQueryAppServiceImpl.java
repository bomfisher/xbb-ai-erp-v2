package xbb.ai.erp.module.purchase.application.service.query;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.application.assembler.ProductAdminAssembler;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.List;
import java.util.Map;

public class PurchaseOrderQueryAppServiceImpl implements PurchaseOrderQueryAppService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderQueryAppServiceImpl(
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    @Override
    public ListBaseVO<PurchaseOrderListItemVO> list(PurchaseOrderListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseOrgId", dto.getPurchaseOrgId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderNo", dto.getOrderNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "vendorId", dto.getVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "vendorNameSnapshot", dto.getVendorNameSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaserId", dto.getPurchaserId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "warehouseId", dto.getWarehouseId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "settlementMethodId", dto.getSettlementMethodId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "currencyCode", dto.getCurrencyCode());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "deliveryDate", dto.getDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceType", dto.getSourceType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceNo", dto.getSourceNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "salesLinkedFlag", dto.getSalesLinkedFlag());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", dto.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "approvalStatus", dto.getApprovalStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "executionStatus", dto.getExecutionStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "receiptStatus", dto.getReceiptStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "inboundStatus", dto.getInboundStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "payableStatus", dto.getPayableStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "invoiceStatus", dto.getInvoiceStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "paymentStatus", dto.getPaymentStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "periodLockedFlag", dto.getPeriodLockedFlag());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
        List<PurchaseOrder> list = purchaseOrderRepository.findByCondition(conditionMap);
        Long total = purchaseOrderRepository.count(conditionMap);
        ListBaseVO<PurchaseOrderListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseOrderAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildHeadList(dto == null ? null : dto.getCorpid()));
        vo.setData(PurchaseOrderAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildHeadList(dto == null ? null : dto.getCorpid()));
        vo.setData(loadSaveItem(dto));
        return vo;
    }

    @Override
    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        return PurchaseOrderAdminAssembler.toDetailVO(loadSaveItem(dto));
    }

    private PurchaseOrderSaveItemVO loadSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseOrder order = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        Map<String, Object> itemConditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(itemConditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(itemConditionMap, "orderId", dto.getId());
        List<PurchaseOrderItem> items = purchaseOrderItemRepository == null ? List.of() : purchaseOrderItemRepository.findByCondition(itemConditionMap);
        return PurchaseOrderAdminAssembler.toSaveItemVO(order, items);
    }

    private List<FieldEntity> buildHeadList(String corpid) {
        return List.of(
            field("main.purchaseOrgId", "采购组织", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.orderNo", "采购订单号", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.vendorId", "供应商ID", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.vendorNameSnapshot", "供应商名称快照", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.purchaserId", "采购员ID", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.purchaserNameSnapshot", "采购员名称快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.warehouseId", "默认收货仓库ID", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.warehouseNameSnapshot", "仓库名称快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.settlementMethodId", "结算方式ID", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.settlementMethodSnapshot", "结算方式快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.paymentTermSnapshot", "付款条件快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.currencyCode", "币种", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.deliveryDate", "交货日期", FieldTypeEnum.DATE.getType(), 0, 1),
            field("main.sourceType", "来源类型", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.sourceNo", "来源单号", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.salesLinkedFlag", "是否以销定购", FieldTypeEnum.NUM_INT.getType(), 0, 1),
            field("main.bizStatus", "业务状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.approvalStatus", "审批状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.executionStatus", "执行状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.receiptStatus", "收料状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.inboundStatus", "入库状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.payableStatus", "应付状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.invoiceStatus", "收票状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.paymentStatus", "付款状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.grossAmount", "含税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.netAmount", "未税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.taxAmount", "税额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.remark", "备注", FieldTypeEnum.TEXT.getType(), 0, 1),
            itemContainer("items", "产品明细", corpid)
        );
    }

    private FieldEntity itemContainer(String attr, String attrName, String corpid) {
        FieldEntity entity = field(attr, attrName, FieldTypeEnum.PRODUCT.getType(), 1, 1);
        entity.setSubField(List.of(
            field("lineNo", "行号", FieldTypeEnum.NUM_INT.getType(), 1, 1),
            productSelectField("skuId", "产品", 1, corpid),
            field("skuCodeSnapshot", "SKU编码快照", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("skuNameSnapshot", "SKU名称快照", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("specSnapshot", "规格快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("purchaseUnitId", "采购单位ID", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("warehouseId", "行级收货仓库ID", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("orderQty", "订单数量", FieldTypeEnum.AMOUNT.getType(), 1, 1),
            field("receivedQty", "已收料数量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("inboundedQty", "已入库数量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("closedQty", "已关闭数量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("returnedQty", "已退料数量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("grossPrice", "含税单价", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("netPrice", "未税单价", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("taxRate", "税率", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("taxAmount", "税额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("grossAmount", "含税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("netAmount", "未税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("payableAmount", "已确认应付金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("paidAmount", "已付金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("invoicedAmount", "已收票金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("isGift", "是否赠品", FieldTypeEnum.NUM_INT.getType(), 0, 1),
            field("deliveryPlanSnapshot", "交货计划快照", FieldTypeEnum.TEXT.getType(), 0, 1)
        ));
        return entity;
    }

    private FieldEntity productSelectField(String attr, String attrName, Integer required, String corpid) {
        FieldEntity entity = field(attr, attrName, FieldTypeEnum.USER.getType(), required, 1);
        entity.setBusinessSelectConfig(ProductAdminAssembler.buildProductBusinessSelectConfig(corpid, "PURCHASE_ORDER"));
        return entity;
    }

    private FieldEntity field(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        FieldEntity entity = new FieldEntity();
        entity.setAttr(attr);
        entity.setAttrName(attrName);
        entity.setFieldType(String.valueOf(fieldType));
        entity.setRequired(required);
        entity.setEditable(editable);
        entity.setItemList(List.of());
        return entity;
    }
}
