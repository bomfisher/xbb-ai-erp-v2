package xbb.ai.erp.module.approval.contract;

/**
 * 业务系统接收审批进行中状态的入口。
 */
public interface ApprovalProgressHandler {

    String businessCode();

    void handle(ApprovalProgressEvent event);
}
