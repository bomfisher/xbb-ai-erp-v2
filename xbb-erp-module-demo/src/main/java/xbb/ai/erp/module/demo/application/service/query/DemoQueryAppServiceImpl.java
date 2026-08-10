package xbb.ai.erp.module.demo.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.admin.dto.DemoBusinessSelectQueryDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoBusinessSelectOptionVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.module.demo.application.schema.DemoListSchemaProvider;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import xbb.ai.erp.module.demo.contract.DemoReferenceItem;
import xbb.ai.erp.module.demo.contract.DemoReferenceQueryApi;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Service
public class DemoQueryAppServiceImpl implements DemoReferenceQueryApi {

    private final DemoRepository demoRepository;
    private final DemoFieldFactory fieldFactory;
    private final DemoListSchemaProvider schemaProvider;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public DemoQueryAppServiceImpl(DemoRepository demoRepository, DemoFieldFactory fieldFactory,
        DemoListSchemaProvider schemaProvider) {
        this.demoRepository = demoRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
    }

    public ListBaseVO<DemoListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Demo> list = demoRepository.findByCondition(conditionMap);
        Long total = demoRepository.count(conditionMap);
        ListBaseVO<DemoListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(DemoAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<DemoSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE).stream().map(SceneFieldAssembler::build).toList());
        vo.setData(DemoAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<DemoSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Demo entity = demoRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE).stream().map(SceneFieldAssembler::build).toList());
        vo.setData(DemoAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public DemoDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Demo entity = demoRepository.findById(dto.getCorpid(), dto.getId());
        return DemoAdminAssembler.toDetailVO(DemoAdminAssembler.toSaveItemVO(entity));
    }

    @Override
    public Map<Long, DemoReferenceItem> findActiveByIds(String corpid, Collection<Long> ids) {
        if (corpid == null || corpid.isBlank() || ids == null || ids.isEmpty()) {
            return Map.of();
        }
        Map<Long, DemoReferenceItem> references = new LinkedHashMap<>();
        demoRepository.findByIds(corpid, ids).forEach(demo ->
            references.put(demo.getId(), new DemoReferenceItem(demo.getId(), demo.getName())));
        return references;
    }

    @Override
    public boolean existsActive(String corpid, Long id) {
        return id != null && demoRepository.findById(corpid, id) != null;
    }

    public List<DemoBusinessSelectOptionVO> businessSelectQuickSearch(DemoBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto);
    }

    public ListBaseVO<DemoBusinessSelectOptionVO> businessSelectDialogSearch(DemoBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<DemoBusinessSelectOptionVO> all = findBusinessSelectOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<DemoBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    public DemoBusinessSelectOptionVO businessSelectGetById(DemoBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        Demo demo = demoRepository.findById(dto.getCorpid(), dto.getId());
        return demo == null ? null : toBusinessSelectOption(demo);
    }

    private List<DemoBusinessSelectOptionVO> findBusinessSelectOptions(DemoBusinessSelectQueryDTO dto) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        if (dto.getId() != null) {
            conditions.put("id", dto.getId());
        }
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        return demoRepository.findByCondition(conditions).stream()
            .filter(item -> keyword.isEmpty() || (item.getName() != null && item.getName().contains(keyword)))
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private DemoBusinessSelectOptionVO toBusinessSelectOption(Demo demo) {
        DemoBusinessSelectOptionVO option = new DemoBusinessSelectOptionVO();
        option.setId(demo.getId());
        option.setName(demo.getName());
        option.setLabel(demo.getName());
        return option;
    }
}
