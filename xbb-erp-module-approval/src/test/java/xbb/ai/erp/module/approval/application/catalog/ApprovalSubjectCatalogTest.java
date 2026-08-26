package xbb.ai.erp.module.approval.application.catalog;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowCatalogVO;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalFieldDefinition;
import xbb.ai.erp.module.approval.contract.ApprovalFieldType;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchemaProvider;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApprovalSubjectCatalogTest {

    @Test
    void 应由业务字段目录生成流程设置页目录() {
        ApprovalSubjectSchemaProvider provider = new ApprovalSubjectSchemaProvider() {
            @Override
            public String businessCode() {
                return "SALES_CONTRACT";
            }

            @Override
            public String businessName() {
                return "销售合同";
            }

            @Override
            public ApprovalSubjectSchema schema(ApprovalScene scene) {
                return new ApprovalSubjectSchema(businessCode(), scene, 1, List.of(
                    new ApprovalFieldDefinition("TOTAL_AMOUNT", "合同金额", "/totalAmount", ApprovalFieldType.DECIMAL,
                        Set.of(ApprovalConditionOperator.GE))
                ));
            }
        };

        ApprovalFlowCatalogVO catalog = new ApprovalSubjectCatalog(List.of(provider)).list().getFirst();

        assertEquals("SALES_CONTRACT", catalog.getBusinessCode());
        assertEquals("销售合同", catalog.getBusinessName());
        assertEquals("AMOUNT", catalog.getFields().getFirst().getFieldType());
        assertEquals(List.of("GE"), catalog.getFields().getFirst().getOperators());
    }

    @Test
    void 不返回未注册到业务编码枚举的审批业务() {
        ApprovalSubjectSchemaProvider provider = new ApprovalSubjectSchemaProvider() {
            @Override
            public String businessCode() {
                return "UNKNOWN_APPROVAL_BUSINESS";
            }

            @Override
            public String businessName() {
                return "未注册业务";
            }

            @Override
            public ApprovalSubjectSchema schema(ApprovalScene scene) {
                return null;
            }
        };

        assertTrue(new ApprovalSubjectCatalog(List.of(provider)).list().isEmpty());
    }
}
