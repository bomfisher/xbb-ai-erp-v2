package xbb.ai.erp.module.demo.sub.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import java.util.Set;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSubmitSaveDTO;
import xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort;

@Component
@RequiredArgsConstructor
public class DemoSubSaveBusinessValidator {
  private final DemoLookupPort demoLookupPort;

  public void validateForSubmit(DemoSubSubmitSaveDTO dto) {
    if (!demoLookupPort.existsActive(dto.getCorpid(), dto.getMain().getDataId())) {
      throw new BizException("关联DEMO不存在或不可用");
    }
    String parentName = dto.getMain().getParentName();
    String currentName = demoLookupPort.findNamesByIds(
        dto.getCorpid(), Set.of(dto.getMain().getDataId())).get(dto.getMain().getDataId());
    if (parentName == null || parentName.isBlank() || !parentName.equals(currentName)) {
      throw new BizException("父级名称与关联DEMO不一致，请重新选择");
    }
  }
}
