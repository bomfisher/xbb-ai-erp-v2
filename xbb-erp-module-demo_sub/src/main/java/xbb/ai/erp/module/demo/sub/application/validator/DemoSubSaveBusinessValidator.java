package xbb.ai.erp.module.demo.sub.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
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
  }
}
