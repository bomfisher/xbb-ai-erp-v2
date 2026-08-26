package xbb.ai.erp.module.purchase.infrastructure.approval;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundApprovalPolicy;
import xbb.ai.erp.module.system.contract.ApprovalConfigKeyEnum;
import xbb.ai.erp.module.system.contract.ApprovalMode;
import xbb.ai.erp.module.system.contract.BusinessConfigQueryApi;

/**
 * 未接入审批配置前默认要求审核，避免提交后意外触发不可逆库存入账。
 */
@Component
@RequiredArgsConstructor
public class DefaultPurchaseInboundApprovalPolicy implements PurchaseInboundApprovalPolicy {

    private final BusinessConfigQueryApi businessConfigQueryApi;

    @Override
    public boolean requiresApproval(String corpid) {
        return businessConfigQueryApi.get(corpid, ApprovalConfigKeyEnum.PURCHASE_INBOUND_APPROVAL_MODE)
            == ApprovalMode.REQUIRED;
    }
}
