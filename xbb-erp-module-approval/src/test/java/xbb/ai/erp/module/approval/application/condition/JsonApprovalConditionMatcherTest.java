package xbb.ai.erp.module.approval.application.condition;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.approval.contract.ApprovalConditionBranch;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalConditionRule;
import xbb.ai.erp.module.approval.contract.ApprovalFieldDefinition;
import xbb.ai.erp.module.approval.contract.ApprovalFieldType;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonApprovalConditionMatcherTest {

    private final JsonApprovalConditionMatcher matcher = new JsonApprovalConditionMatcher(new ObjectMapper());

    @Test
    void matchesDecimalAndDateRulesWithAndRelation() {
        ApprovalSubjectSchema schema = new ApprovalSubjectSchema(
            "CONTRACT",
            ApprovalScene.CREATE,
            1,
            List.of(
                new ApprovalFieldDefinition("TOTAL_AMOUNT", "合同金额", "/totalAmount", ApprovalFieldType.DECIMAL,
                    Set.of(ApprovalConditionOperator.GE)),
                new ApprovalFieldDefinition("SIGN_DATE", "签订日期", "/signDate", ApprovalFieldType.DATE,
                    Set.of(ApprovalConditionOperator.LE))
            )
        );
        ApprovalConditionBranch branch = new ApprovalConditionBranch(
            "high",
            "高金额",
            List.of(
                new ApprovalConditionRule("TOTAL_AMOUNT", ApprovalConditionOperator.GE, "100000"),
                new ApprovalConditionRule("SIGN_DATE", ApprovalConditionOperator.LE, "2026-12-31")
            ),
            false
        );

        assertTrue(matcher.matches(schema, branch, "{\"totalAmount\":120000,\"signDate\":\"2026-08-24\"}"));
        assertFalse(matcher.matches(schema, branch, "{\"totalAmount\":90000,\"signDate\":\"2026-08-24\"}"));
    }
}
