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
 * 销售合同向审批平台注册的可匹配字段目录。
 */
@Component
public class SalesContractApprovalSchemaProvider implements ApprovalSubjectSchemaProvider {

    private static final Set<ApprovalConditionOperator> ID_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.IN
    );
    private static final Set<ApprovalConditionOperator> AMOUNT_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.GT,
        ApprovalConditionOperator.GE,
        ApprovalConditionOperator.LT,
        ApprovalConditionOperator.LE,
        ApprovalConditionOperator.BETWEEN
    );
    private static final Set<ApprovalConditionOperator> ENUM_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.IN
    );
    private static final Set<ApprovalConditionOperator> DATE_OPERATORS = Set.of(
        ApprovalConditionOperator.EQ,
        ApprovalConditionOperator.GT,
        ApprovalConditionOperator.GE,
        ApprovalConditionOperator.LT,
        ApprovalConditionOperator.LE,
        ApprovalConditionOperator.BETWEEN
    );

    @Override
    public String businessCode() {
        return BusinessCodeEnum.SALES_CONTRACT.getCode();
    }

    @Override
    public String businessName() {
        return BusinessCodeEnum.SALES_CONTRACT.getChineseName();
    }

    @Override
    public ApprovalSubjectSchema schema(ApprovalScene scene) {
        return new ApprovalSubjectSchema(businessCode(), scene, 1, List.of(
            new ApprovalFieldDefinition("CUSTOMER_ID", "客户", "/customerId", ApprovalFieldType.INTEGER,
                BusinessCodeEnum.CUSTOMER.getCode(), ID_OPERATORS),
            new ApprovalFieldDefinition("TOTAL_AMOUNT", "合同金额", "/totalAmount", ApprovalFieldType.DECIMAL, AMOUNT_OPERATORS),
            new ApprovalFieldDefinition("CURRENCY_CODE", "币种", "/currencyCode", ApprovalFieldType.ENUM, ENUM_OPERATORS),
            new ApprovalFieldDefinition("SIGN_DATE", "签订日期", "/signDate", ApprovalFieldType.DATE, DATE_OPERATORS)
        ));
    }
}
