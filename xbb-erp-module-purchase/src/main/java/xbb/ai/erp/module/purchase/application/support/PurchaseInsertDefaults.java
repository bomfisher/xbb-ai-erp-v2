package xbb.ai.erp.module.purchase.application.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;

import java.math.BigDecimal;

/**
 * 采购模块新增入库默认值。所有方法只补空值，不覆盖调用方显式传入的值。
 */
public final class PurchaseInsertDefaults {

    public static final long MOCK_REFERENCE_ID = 0L;
    public static final String MOCK_TEXT = "MOCK";
    public static final String MOCK_USER = "MOCK_USER";

    private PurchaseInsertDefaults() {
    }

    public static void apply(PurchaseRequest value, String userId, long now) {
        value.setPurchaseOrgId(defaultLong(value.getPurchaseOrgId()));
        value.setRequestNo(defaultText(value.getRequestNo(), "MOCK-PR-" + now));
        value.setSourceType(defaultText(value.getSourceType(), "manual"));
        value.setBizStatus(defaultText(value.getBizStatus(), "1"));
        value.setApprovalStatus(defaultText(value.getApprovalStatus(), "pending"));
        value.setGrossAmount(defaultDecimal(value.getGrossAmount()));
        value.setNetAmount(defaultDecimal(value.getNetAmount()));
        value.setTaxAmount(defaultDecimal(value.getTaxAmount()));
        applyAudit(value, userId, now);
    }

    public static void apply(PurchaseRequestItem value, String userId, long now, int lineNo) {
        value.setRequestId(defaultLong(value.getRequestId()));
        value.setLineNo(defaultInteger(value.getLineNo(), lineNo));
        value.setSkuId(defaultLong(value.getSkuId()));
        value.setSkuCodeSnapshot(defaultText(value.getSkuCodeSnapshot(), MOCK_TEXT));
        value.setSkuNameSnapshot(defaultText(value.getSkuNameSnapshot(), MOCK_TEXT));
        value.setPurchaseUnitId(defaultLong(value.getPurchaseUnitId()));
        value.setRequestQty(defaultDecimal(value.getRequestQty()));
        value.setReservedQty(defaultDecimal(value.getReservedQty()));
        value.setExecutedQty(defaultDecimal(value.getExecutedQty()));
        value.setClosedQty(defaultDecimal(value.getClosedQty()));
        applyAudit(value, userId, now);
    }

    public static void apply(PurchaseOrder value, String userId, long now) {
        value.setPurchaseOrgId(defaultLong(value.getPurchaseOrgId()));
        value.setOrderNo(defaultText(value.getOrderNo(), "MOCK-PO-" + now));
        value.setVendorId(defaultLong(value.getVendorId()));
        value.setVendorNameSnapshot(defaultText(value.getVendorNameSnapshot(), MOCK_TEXT));
        value.setCurrencyCode(defaultText(value.getCurrencyCode(), "CNY"));
        value.setSalesLinkedFlag(defaultInteger(value.getSalesLinkedFlag(), 0));
        value.setBizStatus(defaultText(value.getBizStatus(), "1"));
        value.setApprovalStatus(defaultText(value.getApprovalStatus(), "pending"));
        value.setExecutionStatus(defaultText(value.getExecutionStatus(), "pending"));
        value.setReceiptStatus(defaultText(value.getReceiptStatus(), "pending"));
        value.setInboundStatus(defaultText(value.getInboundStatus(), "pending"));
        value.setPayableStatus(defaultText(value.getPayableStatus(), "pending"));
        value.setInvoiceStatus(defaultText(value.getInvoiceStatus(), "pending"));
        value.setPaymentStatus(defaultText(value.getPaymentStatus(), "pending"));
        value.setGrossAmount(defaultDecimal(value.getGrossAmount()));
        value.setNetAmount(defaultDecimal(value.getNetAmount()));
        value.setTaxAmount(defaultDecimal(value.getTaxAmount()));
        value.setInboundedQtySummary(defaultDecimal(value.getInboundedQtySummary()));
        value.setUninboundedQtySummary(defaultDecimal(value.getUninboundedQtySummary()));
        value.setClosedQtySummary(defaultDecimal(value.getClosedQtySummary()));
        value.setPayableAmountSummary(defaultDecimal(value.getPayableAmountSummary()));
        value.setPaidAmountSummary(defaultDecimal(value.getPaidAmountSummary()));
        value.setInvoicedAmountSummary(defaultDecimal(value.getInvoicedAmountSummary()));
        value.setPeriodLockedFlag(defaultInteger(value.getPeriodLockedFlag(), 0));
        applyAudit(value, userId, now);
    }

    public static void apply(PurchaseOrderItem value, String userId, long now, int lineNo) {
        value.setOrderId(defaultLong(value.getOrderId()));
        value.setLineNo(defaultInteger(value.getLineNo(), lineNo));
        value.setSkuId(defaultLong(value.getSkuId()));
        value.setSkuCodeSnapshot(defaultText(value.getSkuCodeSnapshot(), MOCK_TEXT));
        value.setSkuNameSnapshot(defaultText(value.getSkuNameSnapshot(), MOCK_TEXT));
        value.setPurchaseUnitId(defaultLong(value.getPurchaseUnitId()));
        value.setOrderQty(defaultDecimal(value.getOrderQty()));
        value.setReceivedQty(defaultDecimal(value.getReceivedQty()));
        value.setInboundedQty(defaultDecimal(value.getInboundedQty()));
        value.setClosedQty(defaultDecimal(value.getClosedQty()));
        value.setReturnedQty(defaultDecimal(value.getReturnedQty()));
        value.setGrossPrice(defaultDecimal(value.getGrossPrice()));
        value.setNetPrice(defaultDecimal(value.getNetPrice()));
        value.setTaxRate(defaultDecimal(value.getTaxRate()));
        value.setTaxAmount(defaultDecimal(value.getTaxAmount()));
        value.setGrossAmount(defaultDecimal(value.getGrossAmount()));
        value.setNetAmount(defaultDecimal(value.getNetAmount()));
        value.setPayableAmount(defaultDecimal(value.getPayableAmount()));
        value.setPaidAmount(defaultDecimal(value.getPaidAmount()));
        value.setInvoicedAmount(defaultDecimal(value.getInvoicedAmount()));
        value.setIsGift(defaultInteger(value.getIsGift(), 0));
        applyAudit(value, userId, now);
    }

    public static void apply(PurchasePendingTask value, String userId, long now) {
        value.setPurchaseOrgId(defaultLong(value.getPurchaseOrgId()));
        value.setTaskNo(defaultText(value.getTaskNo(), "MOCK-TASK-" + now));
        value.setSourceType(defaultText(value.getSourceType(), "manual"));
        value.setSkuId(defaultLong(value.getSkuId()));
        value.setSkuCodeSnapshot(defaultText(value.getSkuCodeSnapshot(), MOCK_TEXT));
        value.setSkuNameSnapshot(defaultText(value.getSkuNameSnapshot(), MOCK_TEXT));
        value.setNeedQty(defaultDecimal(value.getNeedQty()));
        value.setOccupiedQty(defaultDecimal(value.getOccupiedQty()));
        value.setGeneratedRequestQty(defaultDecimal(value.getGeneratedRequestQty()));
        value.setGeneratedOrderQty(defaultDecimal(value.getGeneratedOrderQty()));
        value.setClosedQty(defaultDecimal(value.getClosedQty()));
        value.setPriorityLevel(defaultInteger(value.getPriorityLevel(), 0));
        value.setTaskStatus(defaultText(value.getTaskStatus(), "pending"));
        value.setSalesLinkedFlag(defaultInteger(value.getSalesLinkedFlag(), 0));
        applyAudit(value, userId, now);
    }

    public static void apply(PurchaseSourceRelation value, String userId, long now) {
        value.setSourceDocType(defaultText(value.getSourceDocType(), MOCK_TEXT));
        value.setSourceDocId(defaultLong(value.getSourceDocId()));
        value.setTargetDocType(defaultText(value.getTargetDocType(), MOCK_TEXT));
        value.setTargetDocId(defaultLong(value.getTargetDocId()));
        value.setSourceQty(defaultDecimal(value.getSourceQty()));
        value.setReservedQty(defaultDecimal(value.getReservedQty()));
        value.setExecutedQty(defaultDecimal(value.getExecutedQty()));
        value.setClosedQty(defaultDecimal(value.getClosedQty()));
        value.setReversedQty(defaultDecimal(value.getReversedQty()));
        value.setRelationStatus(defaultText(value.getRelationStatus(), "pending"));
        applyAudit(value, userId, now);
    }

    private static void applyAudit(PurchaseRequest value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static void applyAudit(PurchaseRequestItem value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static void applyAudit(PurchaseOrder value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static void applyAudit(PurchaseOrderItem value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static void applyAudit(PurchasePendingTask value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static void applyAudit(PurchaseSourceRelation value, String userId, long now) {
        value.setVersion(defaultInteger(value.getVersion(), 0));
        value.setDeleted(defaultInteger(value.getDeleted(), 0));
        value.setAddTime(defaultLong(value.getAddTime(), now));
        value.setUpdateTime(defaultLong(value.getUpdateTime(), now));
        value.setCreatorId(defaultText(value.getCreatorId(), defaultText(userId, MOCK_USER)));
        value.setModifyId(defaultText(value.getModifyId(), defaultText(userId, MOCK_USER)));
    }

    private static String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static Long defaultLong(Long value) {
        return defaultLong(value, MOCK_REFERENCE_ID);
    }

    private static Long defaultLong(Long value, long fallback) {
        return value == null ? fallback : value;
    }

    private static Integer defaultInteger(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private static BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
