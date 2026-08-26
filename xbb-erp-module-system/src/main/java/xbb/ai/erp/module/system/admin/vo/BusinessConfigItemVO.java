package xbb.ai.erp.module.system.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.BaseVO;

import java.util.List;

@Data
public class BusinessConfigItemVO extends BaseVO {

    private String code;
    private String title;
    private String helpText;
    private String controlType;
    private Object value;
    private Object defaultValue;
    private List<BusinessConfigOptionVO> options;
}
