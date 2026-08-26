package xbb.ai.erp.module.approval.contract;

/**
 * 业务系统接收审批结束事件的入口。
 *
 * 实现必须依据 {@code eventId} 幂等处理，并在自身事务内创建或原地更新业务数据。
 */
public interface ApprovalResultHandler {

    String businessCode();

    void handle(ApprovalResultEvent event);
}
