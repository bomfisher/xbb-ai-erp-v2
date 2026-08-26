package xbb.ai.erp.module.system.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.BaseVO;

@Data
public class BusinessConfigOptionVO extends BaseVO {

    private String value;
    private String label;
    private String helpText;
}
