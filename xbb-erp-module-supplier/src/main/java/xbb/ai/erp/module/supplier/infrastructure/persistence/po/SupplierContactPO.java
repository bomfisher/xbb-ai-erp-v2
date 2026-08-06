package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier_contact")
public class SupplierContactPO {
    private Long id;
    private String corpid;
    private Long supplierId;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String positionName;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
