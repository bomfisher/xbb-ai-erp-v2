package xbb.ai.erp.module.common.application.render;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.filed.FieldEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListRenderFieldEntity extends FieldEntity {
  private String renderValueAttr;
}
