package xbb.ai.erp.module.demo.sub.admin.dto;

import lombok.Data;

@Data
public class DemoSubMainDTO {
  private Long id;
  private String corpid;
  private Long dataId;
  private String name;
  private String parentName;
  private String userId;
  private Long departmentId;
  private Integer del;
  private Long addTime;
  private Long updateTime;
  private String creatorId;
  private String modifyId;
}
