package xbb.ai.erp.module.approval.application.catalog;

import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowCatalogVO;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalFieldDefinition;
import xbb.ai.erp.module.approval.contract.ApprovalFieldType;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchema;
import xbb.ai.erp.module.approval.contract.ApprovalSubjectSchemaProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApprovalSubjectCatalog {

    private final List<ApprovalSubjectSchemaProvider> schemaProviders;

    public List<ApprovalFlowCatalogVO> list() {
        return schemaProviders.stream()
            .filter(provider -> BusinessCodeEnum.containsCode(provider.businessCode()))
            .map(this::toCatalog)
            .sorted(java.util.Comparator.comparing(ApprovalFlowCatalogVO::getBusinessCode))
            .toList();
    }

    private ApprovalFlowCatalogVO toCatalog(ApprovalSubjectSchemaProvider provider) {
        ApprovalFlowCatalogVO catalog = new ApprovalFlowCatalogVO();
        catalog.setBusinessCode(provider.businessCode());
        catalog.setBusinessName(provider.businessName());
        List<ApprovalScene> scenes = java.util.Arrays.stream(ApprovalScene.values())
            .filter(scene -> provider.schema(scene) != null)
            .toList();
        catalog.setScenes(scenes);
        ApprovalSubjectSchema schema = scenes.stream()
            .map(provider::schema)
            .findFirst()
            .orElse(null);
        catalog.setFields(schema == null ? List.of() : schema.fields().stream().map(this::toField).toList());
        return catalog;
    }

    private ApprovalFlowCatalogVO.FieldVO toField(ApprovalFieldDefinition definition) {
        ApprovalFlowCatalogVO.FieldVO field = new ApprovalFlowCatalogVO.FieldVO();
        field.setAttr(definition.attr());
        field.setName(definition.name());
        field.setFieldType(toFieldType(definition.fieldType()));
        field.setSelectableBusinessCode(definition.selectableBusinessCode());
        field.setOperators(definition.operators().stream().map(Enum::name).sorted().toList());
        return field;
    }

    private String toFieldType(ApprovalFieldType fieldType) {
        return switch (fieldType) {
            case STRING -> "TEXT";
            case DECIMAL -> "AMOUNT";
            case INTEGER -> "NUM_INT";
            case DATE -> "DATE";
            case BOOLEAN -> "SWITCH";
            case ENUM -> "COMB";
        };
    }
}
