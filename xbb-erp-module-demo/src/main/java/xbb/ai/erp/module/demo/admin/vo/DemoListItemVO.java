package xbb.ai.erp.module.demo.admin.vo;

import lombok.Data;

@Data
public class DemoListItemVO {
    private Long id;
    private String name;
    private String userId;
    private String depId;
    private String comb;
    private String combMulti;
    private Integer numInt;
    private java.math.BigDecimal numDouble;
    private java.math.BigDecimal amount;
    private Long date;
    private Long time;
    private String file;
    private String image;
    private String address;
}
