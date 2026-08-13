package xbb.ai.erp.module.purchase.application.port;

/**
 * 判断采购入库单是否需要审核的可替换策略。
 */
public interface PurchaseInboundApprovalPolicy {
    boolean requiresApproval(String corpid);
}
