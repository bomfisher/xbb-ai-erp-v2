package xbb.ai.erp.module.approval.application.condition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchemaProvider;

import java.util.List;

/**
 * 统一管理业务模块注册的审批字段目录。
 */
@Component
@RequiredArgsConstructor
public class ApprovalSubjectSchemaRegistry {

    private final List<ApprovalSubjectSchemaProvider> providers;

    public ApprovalSubjectSchema require(String businessCode, ApprovalScene scene) {
        return providers.stream()
            .filter(provider -> provider.businessCode().equals(businessCode))
            .map(provider -> provider.schema(scene))
            .filter(schema -> schema != null)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "未注册审批业务字段目录: " + businessCode + "/" + scene));
    }
}
