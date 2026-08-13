package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuListItemVO;
import xbb.ai.erp.module.masterdata.application.assembler.ProductSpuAdminAssembler;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.assembler.WarehouseAdminAssembler;
import xbb.ai.erp.module.masterdata.application.field.WarehouseFieldFactory;
import xbb.ai.erp.module.masterdata.application.schema.WarehouseListSchemaProvider;
import xbb.ai.erp.module.masterdata.domain.model.Warehouse;
import xbb.ai.erp.module.masterdata.domain.repository.WarehouseRepository;

@Service
public class WarehouseQueryAppServiceImpl {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseFieldFactory fieldFactory;
    private final WarehouseListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public WarehouseQueryAppServiceImpl(WarehouseRepository warehouseRepository, WarehouseFieldFactory fieldFactory, WarehouseListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.warehouseRepository = warehouseRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<WarehouseListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Warehouse> list = warehouseRepository.findByCondition(conditionMap);
        Long total = warehouseRepository.count(conditionMap);
        ListBaseVO<WarehouseListItemVO> vo = new ListBaseVO<>();
        List<WarehouseListItemVO> items = list.stream().map(WarehouseAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.WAREHOUSE.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(WarehouseAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Warehouse entity = warehouseRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(WarehouseAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public WarehouseDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Warehouse entity = warehouseRepository.findById(dto.getCorpid(), dto.getId());
        return WarehouseAdminAssembler.toDetailVO(WarehouseAdminAssembler.toSaveItemVO(entity));
    }

    public List<WarehouseBusinessSelectOptionVO> businessSelectQuickSearch(WarehouseBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto);
    }

    public ListBaseVO<WarehouseBusinessSelectOptionVO> businessSelectDialogSearch(WarehouseBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<WarehouseBusinessSelectOptionVO> all = findBusinessSelectOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<WarehouseBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    public WarehouseBusinessSelectOptionVO businessSelectGetById(WarehouseBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        Warehouse warehouse = warehouseRepository.findById(dto.getCorpid(), dto.getId());
        return warehouse == null ? null : toBusinessSelectOption(warehouse);
    }

    private List<WarehouseBusinessSelectOptionVO> findBusinessSelectOptions(WarehouseBusinessSelectQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        return warehouseRepository.findByCondition(conditions).stream()
                .filter(warehouse -> keyword.isEmpty()
                        || (warehouse.getWarehouseCode() != null && warehouse.getWarehouseCode().contains(keyword))
                        || (warehouse.getWarehouseName() != null && warehouse.getWarehouseName().contains(keyword)))
                .map(this::toBusinessSelectOption)
                .toList();
    }

    private WarehouseBusinessSelectOptionVO toBusinessSelectOption(Warehouse warehouse) {
        WarehouseBusinessSelectOptionVO option = new WarehouseBusinessSelectOptionVO();
        option.setId(warehouse.getId());
        option.setCode(warehouse.getWarehouseCode());
        option.setName(warehouse.getWarehouseName());
        option.setLabel(warehouse.getWarehouseCode() == null || warehouse.getWarehouseCode().isBlank()
                ? warehouse.getWarehouseName()
                : warehouse.getWarehouseCode() + " " + warehouse.getWarehouseName());
        return option;
    }
}
