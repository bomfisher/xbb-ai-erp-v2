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
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.application.assembler.SupplierAdminAssembler;
import xbb.ai.erp.module.masterdata.application.field.SupplierFieldFactory;
import xbb.ai.erp.module.masterdata.application.schema.SupplierListSchemaProvider;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;

@Service
public class SupplierQueryAppServiceImpl {
    private final SupplierRepository supplierRepository;
    private final SupplierFieldFactory fieldFactory;
    private final SupplierListSchemaProvider schemaProvider;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SupplierQueryAppServiceImpl(SupplierRepository supplierRepository, SupplierFieldFactory fieldFactory, SupplierListSchemaProvider schemaProvider) {
        this.supplierRepository = supplierRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider;
    }

    public ListBaseVO<SupplierListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Supplier> list = supplierRepository.findByCondition(conditionMap);
        Long total = supplierRepository.count(conditionMap);
        ListBaseVO<SupplierListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(SupplierAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(SupplierAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Supplier entity = supplierRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(SupplierAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public SupplierDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Supplier entity = supplierRepository.findById(dto.getCorpid(), dto.getId());
        return SupplierAdminAssembler.toDetailVO(SupplierAdminAssembler.toSaveItemVO(entity));
    }
}
