package xbb.ai.erp.module.demo.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.validator.DemoValidator;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;
import xbb.ai.erp.module.demo.application.validator.DemoSaveProtocolValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveCommonValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveBusinessValidator;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;

@Service
@RequiredArgsConstructor
public class DemoSaveAppServiceImpl {

    private final DemoRepository demoRepository;

    private final DemoDraftRepository draftRepository;
    private final DemoSaveProtocolValidator protocolValidator;
    private final DemoSaveCommonValidator commonValidator;
    private final DemoSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(DemoSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(DemoSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        DemoValidator.validateSave(dto);
        Demo entity = DemoAdminAssembler.toDemo(dto);
        long now = System.currentTimeMillis();
        entity.setCreatorId(dto.getUserId());
        entity.setModifyId(dto.getUserId());
        entity.setAddTime(now);
        entity.setUpdateTime(now);
        entity.setDel(0);
        if (entity.getId() == null) {
            demoRepository.insert(entity);
        } else {
            demoRepository.update(entity);
        }
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            demoRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
