package xbb.ai.erp.module.demo.application.service.save;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.validator.DemoValidator;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;

@Service
public class DemoSaveAppServiceImpl {

    private final DemoRepository demoRepository;

    public DemoSaveAppServiceImpl(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public Long save(DemoSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        DemoValidator.validateSave(dto);
        Demo entity = DemoAdminAssembler.toDemo(dto);
        if (entity.getId() == null) {
            demoRepository.insert(entity);
        } else {
            demoRepository.update(entity);
        }
        return entity.getId();
    }
}
