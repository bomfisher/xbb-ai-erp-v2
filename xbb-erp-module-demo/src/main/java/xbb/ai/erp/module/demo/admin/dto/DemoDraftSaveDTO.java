package xbb.ai.erp.module.demo.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoDraftSaveDTO extends DemoSaveDTO {
    private DemoDraftMetaDTO draftMeta = new DemoDraftMetaDTO();
}
