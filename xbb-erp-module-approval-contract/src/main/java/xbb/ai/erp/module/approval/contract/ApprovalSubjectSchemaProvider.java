package xbb.ai.erp.module.approval.contract;

/**
 * 业务模块提供审批字段目录的扩展点。
 */
public interface ApprovalSubjectSchemaProvider {

    String businessCode();

    default String businessName() {
        return businessCode();
    }

    ApprovalSubjectSchema schema(ApprovalScene scene);
}
