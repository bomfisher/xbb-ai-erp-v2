package xbb.ai.erp.module.demo.domain.model;

import lombok.Data;

@Data
public class DemoSub {
    private Long id;
    private String corpid;
    private Long dataId;
    private String name;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
