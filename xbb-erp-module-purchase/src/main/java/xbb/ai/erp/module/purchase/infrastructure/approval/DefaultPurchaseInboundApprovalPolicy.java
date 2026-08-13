package xbb.ai.erp.module.purchase.infrastructure.approval;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundApprovalPolicy;

/**
 * 未接入审批配置前默认要求审核，避免提交后意外触发不可逆库存入账。
 */
@Component
public class DefaultPurchaseInboundApprovalPolicy implements PurchaseInboundApprovalPolicy {
    @Override
    public boolean requiresApproval(String corpid) {
        return true;
    }
}
