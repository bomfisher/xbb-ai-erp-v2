package xbb.ai.erp.module.system.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.BaseVO;

import java.util.List;

@Data
public class BusinessConfigDocumentVO extends BaseVO {

    private String categoryCode;
    private String categoryName;
    private String businessCode;
    private String businessName;
    private List<BusinessConfigItemVO> items;
}
