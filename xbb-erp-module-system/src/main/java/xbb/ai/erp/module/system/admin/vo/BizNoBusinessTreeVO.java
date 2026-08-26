package xbb.ai.erp.module.system.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.BaseVO;

import java.util.List;

@Data
public class BizNoBusinessTreeVO extends BaseVO {

    private String businessCode;
    private String businessName;
    private Integer overridden;
    private List<BizNoBusinessTreeVO> children;
}
