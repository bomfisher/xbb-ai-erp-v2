package xbb.ai.erp.base.common.vo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

/**
 * 新建或编辑页接口的返回对象
 * @param <T>
 */
@Data
public class SaveItemVO<T> {
    private List<FieldEntity> headList;

    /**
     * 默认值或历史值
     */
    private T data;
}
