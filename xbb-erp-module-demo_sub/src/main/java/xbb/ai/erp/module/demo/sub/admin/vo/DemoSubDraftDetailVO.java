package xbb.ai.erp.module.demo.sub.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubMainDTO;

@Data
public class DemoSubDraftDetailVO {
  private String draftCode;
  private DemoSubMainDTO main;
}
