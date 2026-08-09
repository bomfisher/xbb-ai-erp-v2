package xbb.ai.erp.module.demo.sub.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoSubDraftSaveDTO extends DemoSubSaveDTO {
  private DemoSubDraftMetaDTO draftMeta = new DemoSubDraftMetaDTO();
}
