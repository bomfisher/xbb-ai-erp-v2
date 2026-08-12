package xbb.ai.erp.module.demo.sub.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoSubSubmitSaveDTO extends DemoSubSaveDTO {
  private DemoSubDraftMetaDTO draftMeta = new DemoSubDraftMetaDTO();
}
