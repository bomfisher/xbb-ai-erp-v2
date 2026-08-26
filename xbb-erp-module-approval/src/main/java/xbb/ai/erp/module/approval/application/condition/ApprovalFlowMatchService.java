package xbb.ai.erp.module.approval.application.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.approval.contract.ApprovalConditionBranch;
import xbb.ai.erp.module.approval.contract.ApprovalConditionMatcher;
import xbb.ai.erp.module.approval.contract.ApprovalConditionOperator;
import xbb.ai.erp.module.approval.contract.ApprovalConditionRule;
import xbb.ai.erp.module.approval.contract.ApprovalFlowMatch;
import xbb.ai.erp.module.approval.contract.ApprovalFlowMatchApi;
import xbb.ai.erp.module.approval.contract.ApprovalSubmitCommand;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowStatus;
import xbb.ai.erp.module.approval.domain.repository.ApprovalFlowRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 按发布流程的适用数据条件匹配审批流程。
 */
@Service
@RequiredArgsConstructor
public class ApprovalFlowMatchService implements ApprovalFlowMatchApi {

    private final ApprovalFlowRepository approvalFlowRepository;
    private final ApprovalSubjectSchemaRegistry schemaRegistry;
    private final ApprovalConditionMatcher conditionMatcher;
    private final ObjectMapper objectMapper;

    @Override
    public ApprovalFlowMatch match(ApprovalSubmitCommand command) {
        ApprovalSubjectSchema schema = schemaRegistry.require(command.businessCode(), command.scene());
        return approvalFlowRepository
            .findByCondition(command.tenantId(), command.businessCode(), command.scene())
            .stream()
            .filter(flow -> flow.getStatus() == ApprovalFlowStatus.PUBLISHED)
            .filter(flow -> matchesDataConditions(schema, flow, command.subjectSnapshotJson()))
            .findFirst()
            .map(flow -> new ApprovalFlowMatch(flow.getId(), flow.getFlowCode(), flow.getVersion()))
            .orElseGet(ApprovalFlowMatch::noApproval);
    }

    private boolean matchesDataConditions(ApprovalSubjectSchema schema, ApprovalFlowDefinition flow, String snapshotJson) {
        ApprovalConditionBranch applicability = readApplicability(flow.getScopeJson());
        return applicability.rules().isEmpty() || conditionMatcher.matches(schema, applicability, snapshotJson);
    }

    private ApprovalConditionBranch readApplicability(String scopeJson) {
        try {
            JsonNode conditions = objectMapper.readTree(scopeJson).path("applicability").path("conditions");
            List<ApprovalConditionRule> rules = new ArrayList<>();
            for (JsonNode condition : conditions) {
                rules.add(new ApprovalConditionRule(
                    condition.path("fieldAttr").asText(),
                    ApprovalConditionOperator.valueOf(condition.path("operator").asText()),
                    condition.path("value").isTextual()
                        ? condition.path("value").asText()
                        : objectMapper.writeValueAsString(condition.path("value"))
                ));
            }
            return new ApprovalConditionBranch("applicability", "适用数据条件", rules, false);
        } catch (Exception exception) {
            throw new BizException("审批流程适用条件格式错误");
        }
    }
}
