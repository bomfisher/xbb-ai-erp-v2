package xbb.ai.erp.module.demo.sub.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.demo.sub.admin.dto.*;
import xbb.ai.erp.module.demo.sub.application.assembler.DemoSubAdminAssembler;
import xbb.ai.erp.module.demo.sub.application.port.DemoSubDraftRepository;
import xbb.ai.erp.module.demo.sub.application.validator.*;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.domain.repository.DemoSubRepository;

@Service
@RequiredArgsConstructor
public class DemoSubSaveAppServiceImpl {
  private final DemoSubRepository repository;
  private final DemoSubDraftRepository draftRepository;
  private final DemoSubSaveProtocolValidator protocolValidator = new DemoSubSaveProtocolValidator();
  private final DemoSubSaveCommonValidator commonValidator = new DemoSubSaveCommonValidator();
  private final DemoSubSaveBusinessValidator businessValidator = new DemoSubSaveBusinessValidator();

  @Transactional
  public BaseVO saveAndSubmit(DemoSubSubmitSaveDTO dto) {
    protocolValidator.validate(dto);
    commonValidator.validateForSubmit(dto);
    businessValidator.validateForSubmit(dto);
    save(dto);
    if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null)
      draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
    return new BaseVO();
  }

  public Long save(DemoSubSaveDTO dto) {
    DemoSub entity = DemoSubAdminAssembler.toDemoSub(dto);
    long now = System.currentTimeMillis();
    entity.setCreatorId(dto.getUserId());
    entity.setModifyId(dto.getUserId());
    entity.setAddTime(now);
    entity.setUpdateTime(now);
    entity.setDeleted(0);
    if (entity.getId() == null) repository.insert(entity);
    else repository.update(entity);
    return entity.getId();
  }

  public void delete(BatchBaseDTO dto) {
    if (dto.getIdList() != null && !dto.getIdList().isEmpty())
      repository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
  }
}
