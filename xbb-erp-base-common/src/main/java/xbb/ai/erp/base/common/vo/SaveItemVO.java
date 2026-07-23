package xbb.ai.erp.base.common.vo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

@Data
public class SaveItemVO<T> {
    private List<FieldEntity> headList;

    private T data;
}
