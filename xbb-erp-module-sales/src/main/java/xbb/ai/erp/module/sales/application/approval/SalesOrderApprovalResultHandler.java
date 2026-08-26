package xbb.ai.erp.module.sales.application.approval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.module.approval.contract.ApprovalResult;
import xbb.ai.erp.module.approval.contract.ApprovalResultEvent;
import xbb.ai.erp.module.approval.contract.ApprovalResultHandler;
import xbb.ai.erp.module.approval.contract.ApprovalProgressEvent;
import xbb.ai.erp.module.approval.contract.ApprovalProgressHandler;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.sales.application.service.save.SalesOrderSaveAppServiceImpl;

@Component
@RequiredArgsConstructor
public class SalesOrderApprovalResultHandler implements ApprovalResultHandler, ApprovalProgressHandler {

    private final SalesOrderSaveAppServiceImpl salesOrderSaveAppService;

    @Override
    public String businessCode() {
        return BusinessCodeEnum.SALES_ORDER.getCode();
    }

    @Override
    @Transactional
    public void handle(ApprovalResultEvent event) {
        if (event.result() == ApprovalResult.APPROVED) {
            salesOrderSaveAppService.activateApprovedOrder(event.tenantId(), Long.valueOf(event.subjectId()), event.instanceId());
            return;
        }
        salesOrderSaveAppService.finishUnapprovedOrder(event.tenantId(), Long.valueOf(event.subjectId()));
    }

    @Override
    @Transactional
    public void handle(ApprovalProgressEvent event) {
        salesOrderSaveAppService.markOrderApprovalProcessing(event.tenantId(), Long.valueOf(event.subjectId()),
            event.instanceId());
    }
}
