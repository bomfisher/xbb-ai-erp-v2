package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;

@Data
public class SupplierQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String supplierCode;
    private String supplierName;
    private Integer enabled;
    private String remark;
}
