package xbb.ai.erp.module.approval.contract;

import java.util.List;

/**
 * 业务对象审批字段目录。
 */
public record ApprovalSubjectSchema(
    String businessCode,
    ApprovalScene scene,
    Integer version,
    List<ApprovalFieldDefinition> fields
) {
}
