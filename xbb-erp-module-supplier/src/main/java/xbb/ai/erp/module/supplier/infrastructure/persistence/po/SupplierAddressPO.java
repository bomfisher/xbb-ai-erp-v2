package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier_address")
public class SupplierAddressPO {
    private Long id;
    private String corpid;
    private Long supplierId;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
    private String postalCode;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
