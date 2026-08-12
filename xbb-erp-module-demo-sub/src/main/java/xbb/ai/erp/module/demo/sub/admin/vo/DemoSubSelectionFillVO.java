package xbb.ai.erp.module.demo.sub.admin.vo;

import java.util.Map;
import lombok.Data;

@Data
public class DemoSubSelectionFillVO {
  private Long referenceId;
  private Map<String, Object> patch;
}
