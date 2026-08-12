package xbb.ai.erp.module.masterdata.domain.pojo;

import lombok.Data;

@Data
public class CustomerQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String customerCode;
    private String customerName;
    private Integer enabled;
}
