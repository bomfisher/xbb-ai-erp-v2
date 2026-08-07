package xbb.ai.erp.module.demo.application.service.query;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.assembler.DemoFieldAssembler;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DemoQueryAppServiceImpl {

    private final DemoRepository demoRepository;
    private final DemoFieldFactory demoFieldFactory;

    public DemoQueryAppServiceImpl(DemoRepository demoRepository, DemoFieldFactory demoFieldFactory) {
        this.demoRepository = demoRepository;
        this.demoFieldFactory = demoFieldFactory;
    }

    public ListBaseVO<DemoListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("keyword", dto.getKeyword());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("conditions", dto.getConditions());
        List<Demo> list = demoRepository.findByCondition(conditionMap);
        Long total = demoRepository.count(conditionMap);
        ListBaseVO<DemoListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(DemoAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<DemoSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(DemoFieldAssembler.buildHeadList(demoFieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(DemoAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<DemoSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Demo entity = demoRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(DemoFieldAssembler.buildHeadList(demoFieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(DemoAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public DemoDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Demo entity = demoRepository.findById(dto.getCorpid(), dto.getId());
        return DemoAdminAssembler.toDetailVO(DemoAdminAssembler.toSaveItemVO(entity));
    }
}
