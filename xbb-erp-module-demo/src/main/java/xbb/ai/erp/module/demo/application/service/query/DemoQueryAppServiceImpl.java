package xbb.ai.erp.module.demo.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
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
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Service
public class DemoQueryAppServiceImpl {

    private final DemoRepository demoRepository;
    private final DemoItemRepository demoItemRepository;
    private final DemoFieldFactory fieldFactory;
    private final DemoListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public DemoQueryAppServiceImpl(DemoRepository demoRepository, DemoItemRepository demoItemRepository, DemoFieldFactory fieldFactory,
        DemoListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.demoRepository = demoRepository;
        this.demoItemRepository = demoItemRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<DemoListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Demo> list = demoRepository.findByCondition(conditionMap);
        Long total = demoRepository.count(conditionMap);
        ListBaseVO<DemoListItemVO> vo = new ListBaseVO<>();
        List<DemoListItemVO> items = list.stream().map(DemoAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.DEMO.getCode(), items));
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
        DemoSaveItemVO data = DemoAdminAssembler.toSaveItemVO(entity);
        List<xbb.ai.erp.module.demo.domain.model.DemoItem> allItems = demoItemRepository.findByDataId(dto.getCorpid(), dto.getId());
        data.setItems(DemoAdminAssembler.toDemoItemDTOs(allItems.stream()
            .filter(item -> item.getName() == null || !item.getName().endsWith("-2")).toList()));
        data.setItems2(DemoAdminAssembler.toDemoItemDTOs(allItems.stream()
            .filter(item -> item.getName() != null && item.getName().endsWith("-2")).toList()));
        vo.setData(data);
        return vo;
    }

    public DemoDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Demo entity = demoRepository.findById(dto.getCorpid(), dto.getId());
        return DemoAdminAssembler.toDetailVO(DemoAdminAssembler.toSaveItemVO(entity));
    }

    public List<DemoBusinessSelectOptionVO> businessSelectQuickSearch(DemoBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<DemoBusinessSelectOptionVO> businessSelectDialogSearch(DemoBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<DemoBusinessSelectOptionVO> options = findBusinessSelectOptions(dto, (pageNum - 1) * pageSize, pageSize);
        Long total = demoRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<DemoBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public DemoBusinessSelectOptionVO businessSelectGetById(DemoBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        Demo demo = demoRepository.findById(dto.getCorpid(), dto.getId());
        return demo == null ? null : toBusinessSelectOption(demo);
    }

    private List<DemoBusinessSelectOptionVO> findBusinessSelectOptions(DemoBusinessSelectQueryDTO dto,
                                                                         Integer offset, Integer pageSize) {
        return demoRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(DemoBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        if (dto.getId() != null) {
            conditions.put("id", dto.getId());
        }
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private DemoBusinessSelectOptionVO toBusinessSelectOption(Demo demo) {
        DemoBusinessSelectOptionVO option = new DemoBusinessSelectOptionVO();
        option.setId(demo.getId());
        option.setName(demo.getName());
        option.setLabel(demo.getName());
        return option;
    }
}
