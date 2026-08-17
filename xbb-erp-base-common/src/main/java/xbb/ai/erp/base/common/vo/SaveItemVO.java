package xbb.ai.erp.base.common.vo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FormSectionEntity;

import java.util.List;
import java.util.Map;

@Data
public class SaveItemVO<T> {
    private List<FieldEntity> headList;

    private List<FormSectionEntity> formSections;

    private Map<String, Object> linkageConfig;

    private T data;
}
