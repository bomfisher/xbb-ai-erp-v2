package xbb.ai.erp.module.sales.application.approval;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalFieldDefinition;
import xbb.ai.erp.module.approval.contract.ApprovalFieldType;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchemaProvider;

import java.util.List;
import java.util.Set;

/**
 * 销售订单向审批平台注册的可匹配字段目录。
 */
@Component
public class SalesOrderApprovalSchemaProvider implements ApprovalSubjectSchemaProvider {

    private static final Set<ApprovalConditionOperator> TEXT_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.CONTAINS
    );
    private static final Set<ApprovalConditionOperator> BUSINESS_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.IN
    );
    private static final Set<ApprovalConditionOperator> DATE_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.GE,
        ApprovalConditionOperator.LE,
        ApprovalConditionOperator.BETWEEN
    );
    private static final Set<ApprovalConditionOperator> AMOUNT_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.GT,
        ApprovalConditionOperator.GE,
        ApprovalConditionOperator.LT,
        ApprovalConditionOperator.LE,
        ApprovalConditionOperator.BETWEEN
    );

    @Override
    public String businessCode() {
        return BusinessCodeEnum.SALES_ORDER.getCode();
    }

    @Override
    public String businessName() {
        return BusinessCodeEnum.SALES_ORDER.getChineseName();
    }

    @Override
    public ApprovalSubjectSchema schema(ApprovalScene scene) {
        return new ApprovalSubjectSchema(businessCode(), scene, 1, List.of(
            new ApprovalFieldDefinition("main.orderNo", "对接编号", "/main/orderNo", ApprovalFieldType.STRING, TEXT_OPERATORS),
            new ApprovalFieldDefinition("main.customerId", "客户", "/main/customerId", ApprovalFieldType.INTEGER,
                BusinessCodeEnum.CUSTOMER.getCode(), BUSINESS_OPERATORS),
            new ApprovalFieldDefinition("main.warehouseId", "快捷选择仓库", "/main/warehouseId", ApprovalFieldType.INTEGER,
                BusinessCodeEnum.WAREHOUSE.getCode(), BUSINESS_OPERATORS),
            new ApprovalFieldDefinition("main.orderDate", "下单日期", "/main/orderDate", ApprovalFieldType.DATE, DATE_OPERATORS),
            new ApprovalFieldDefinition("main.deliveryDate", "交货日期", "/main/deliveryDate", ApprovalFieldType.DATE, DATE_OPERATORS),
            new ApprovalFieldDefinition("main.totalAmount", "订单金额", "/main/totalAmount", ApprovalFieldType.DECIMAL, AMOUNT_OPERATORS),
            new ApprovalFieldDefinition("main.remark", "备注", "/main/remark", ApprovalFieldType.STRING, TEXT_OPERATORS)
        ));
    }
}
