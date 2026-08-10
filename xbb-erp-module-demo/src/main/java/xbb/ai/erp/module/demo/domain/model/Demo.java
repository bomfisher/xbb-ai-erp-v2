package xbb.ai.erp.module.demo.domain.model;

import lombok.Data;

@Data
public class Demo {
    private Long id;
    private String corpid;
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
    private String creatorId;
    private String modifyId;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
