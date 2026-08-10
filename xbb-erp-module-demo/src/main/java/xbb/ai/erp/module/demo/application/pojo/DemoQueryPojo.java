package xbb.ai.erp.module.demo.application.pojo;

import lombok.Data;

@Data
public class DemoQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String name;
    private String userId;
    private String depId;
    private String comb;
    private Integer numInt;
    private java.math.BigDecimal numDouble;
    private java.math.BigDecimal amount;
    private Long date;
    private Long time;
}
