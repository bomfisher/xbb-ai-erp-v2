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
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierBusinessSelectOptionVO;
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
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SupplierQueryAppServiceImpl(SupplierRepository supplierRepository, SupplierFieldFactory fieldFactory, SupplierListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.supplierRepository = supplierRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<SupplierListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Supplier> list = supplierRepository.findByCondition(conditionMap);
        Long total = supplierRepository.count(conditionMap);
        ListBaseVO<SupplierListItemVO> vo = new ListBaseVO<>();
        List<SupplierListItemVO> items = list.stream().map(SupplierAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.SUPPLIER.getCode(), items));
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

    public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto);
    }

    public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<SupplierBusinessSelectOptionVO> all = findBusinessSelectOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<SupplierBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        Supplier supplier = supplierRepository.findById(dto.getCorpid(), dto.getId());
        return supplier == null ? null : toBusinessSelectOption(supplier);
    }

    private List<SupplierBusinessSelectOptionVO> findBusinessSelectOptions(SupplierBusinessSelectQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        return supplierRepository.findByCondition(conditions).stream()
                .filter(supplier -> keyword.isEmpty()
                        || (supplier.getSupplierCode() != null && supplier.getSupplierCode().contains(keyword))
                        || (supplier.getSupplierName() != null && supplier.getSupplierName().contains(keyword)))
                .map(this::toBusinessSelectOption)
                .toList();
    }

    private SupplierBusinessSelectOptionVO toBusinessSelectOption(Supplier supplier) {
        SupplierBusinessSelectOptionVO option = new SupplierBusinessSelectOptionVO();
        option.setId(supplier.getId());
        option.setCode(supplier.getSupplierCode());
        option.setName(supplier.getSupplierName());
        option.setLabel(supplier.getSupplierCode() == null || supplier.getSupplierCode().isBlank()
                ? supplier.getSupplierName()
                : supplier.getSupplierCode() + " " + supplier.getSupplierName());
        return option;
    }
}
