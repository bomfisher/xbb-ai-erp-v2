package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.approval.contract.ApprovalScene;

import java.util.List;

@Data
public class ApprovalFlowCatalogVO {
    private String businessCode;
    private String businessName;
    private List<ApprovalScene> scenes;
    private List<FieldVO> fields;

    @Data
    public static class FieldVO {
        private String attr;
        private String name;
        private String fieldType;
        private String selectableBusinessCode;
        private List<String> operators;
    }
}
