package xbb.ai.erp.module.demo.domain.pojo;

import lombok.Data;

@Data
public class DemoItemQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private Long dataId;
    private String name;
}
