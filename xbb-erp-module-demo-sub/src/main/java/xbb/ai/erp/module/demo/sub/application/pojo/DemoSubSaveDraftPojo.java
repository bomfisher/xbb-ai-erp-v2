package xbb.ai.erp.module.demo.sub.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubMainDTO;

@Data
public class DemoSubSaveDraftPojo {
  private String corpid;
  private String draftCode;
  private String draftTitle;
  private DemoSubMainDTO main;
  private Long updatedTime;
}
