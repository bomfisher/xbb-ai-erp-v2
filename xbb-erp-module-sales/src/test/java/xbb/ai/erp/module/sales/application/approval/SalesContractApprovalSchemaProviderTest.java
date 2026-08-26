package xbb.ai.erp.module.sales.application.approval;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesContractApprovalSchemaProviderTest {

    private final SalesContractApprovalSchemaProvider provider = new SalesContractApprovalSchemaProvider();

    @Test
    void 应暴露合同审批条件字段目录() {
        ApprovalSubjectSchema schema = provider.schema(ApprovalScene.CREATE);

        assertEquals("SALES_CONTRACT", provider.businessCode());
        assertEquals("销售合同", provider.businessName());
        assertEquals(4, schema.fields().size());
        assertTrue(schema.fields().stream().anyMatch(field -> field.attr().equals("TOTAL_AMOUNT")
            && field.operators().contains(ApprovalConditionOperator.BETWEEN)));
        assertTrue(schema.fields().stream().anyMatch(field -> field.attr().equals("CUSTOMER_ID")
            && "CUSTOMER".equals(field.selectableBusinessCode())));
    }
}
