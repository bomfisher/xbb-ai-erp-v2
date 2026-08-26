package xbb.ai.erp.module.approval.contract;

import java.util.Set;

/**
 * 业务对象可供审批条件使用的字段定义。
 *
 * @param attr 稳定字段编码
 * @param name 字段名称
 * @param path 快照中的 JSON Pointer 路径
 * @param fieldType 字段类型
 * @param selectableBusinessCode 关联选择业务编码；非选择字段为空
 * @param operators 允许的操作符
 */
public record ApprovalFieldDefinition(
    String attr,
    String name,
    String path,
    ApprovalFieldType fieldType,
    String selectableBusinessCode,
    Set<ApprovalConditionOperator> operators
) {

    public ApprovalFieldDefinition(
        String attr,
        String name,
        String path,
        ApprovalFieldType fieldType,
        Set<ApprovalConditionOperator> operators
    ) {
        this(attr, name, path, fieldType, null, operators);
    }
}
