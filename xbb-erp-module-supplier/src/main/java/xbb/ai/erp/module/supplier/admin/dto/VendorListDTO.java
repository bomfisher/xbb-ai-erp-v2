package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class VendorListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private String vendorCode;
    private String vendorName;
    private String vendorShortName;
    private String vendorCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String bizStatus;
    private String refStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
