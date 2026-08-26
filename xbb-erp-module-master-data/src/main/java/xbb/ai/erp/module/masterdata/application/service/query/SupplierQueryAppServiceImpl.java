package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
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
    private final BizNoGenerator bizNoGenerator;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SupplierQueryAppServiceImpl(SupplierRepository supplierRepository, SupplierFieldFactory fieldFactory, SupplierListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator) {
        this.supplierRepository = supplierRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
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
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO data = SupplierAdminAssembler.buildEmptySaveItemVO();
        data.getMain().setSupplierCode(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SUPPLIER.getCode()));
        vo.setData(data);
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
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<SupplierBusinessSelectOptionVO> options = findBusinessSelectOptions(dto, (pageNum - 1) * pageSize, pageSize);
        Long total = supplierRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<SupplierBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
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

    private List<SupplierBusinessSelectOptionVO> findBusinessSelectOptions(SupplierBusinessSelectQueryDTO dto,
                                                                             Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return supplierRepository.findByCondition(conditions).stream()
                .map(this::toBusinessSelectOption)
                .toList();
    }

    private static Map<String, Object> businessSelectConditions(SupplierBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
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
