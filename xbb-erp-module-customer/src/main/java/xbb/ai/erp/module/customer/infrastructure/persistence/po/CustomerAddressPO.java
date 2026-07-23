package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_address")
public class CustomerAddressPO extends BaseEntity {
    private String corpid;
    private Long customerId;
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
    private Integer version;
}
