package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
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
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public WarehouseQueryAppServiceImpl(WarehouseRepository warehouseRepository, WarehouseFieldFactory fieldFactory, WarehouseListSchemaProvider schemaProvider) {
        this.warehouseRepository = warehouseRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider;
    }

    public ListBaseVO<WarehouseListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Warehouse> list = warehouseRepository.findByCondition(conditionMap);
        Long total = warehouseRepository.count(conditionMap);
        ListBaseVO<WarehouseListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(WarehouseAdminAssembler::toListItemVO).toList());
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
}
