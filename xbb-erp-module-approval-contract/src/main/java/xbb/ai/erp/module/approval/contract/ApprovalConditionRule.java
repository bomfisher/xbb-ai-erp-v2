package xbb.ai.erp.module.approval.contract;

/**
 * 单条审批条件。value 使用配置协议中的字符串；BETWEEN 和 IN 使用 JSON 数组字符串。
 */
public record ApprovalConditionRule(
    String fieldAttr,
    ApprovalConditionOperator operator,
    String value
) {
}
