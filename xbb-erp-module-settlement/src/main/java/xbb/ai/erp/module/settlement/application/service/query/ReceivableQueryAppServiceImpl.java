package xbb.ai.erp.module.settlement.application.service.query;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableListItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableMainDTO;
import xbb.ai.erp.module.settlement.admin.ReceivableSourceTypeEnum;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableSaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableBusinessSelectQueryDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableBusinessSelectOptionVO;
import xbb.ai.erp.module.settlement.application.assembler.ReceivableAdminAssembler;
import xbb.ai.erp.module.settlement.application.field.ReceivableFieldFactory;
import xbb.ai.erp.module.settlement.application.field.ReceivableFormSectionFactory;
import xbb.ai.erp.module.settlement.application.schema.ReceivableListSchemaProvider;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;

@Service
public class ReceivableQueryAppServiceImpl {
    private final ReceivableRepository receivableRepository;
    private final ReceivableFieldFactory fieldFactory;
    private final ReceivableListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final SalesInvoiceOpenAmountApi salesInvoiceOpenAmountApi;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public ReceivableQueryAppServiceImpl(ReceivableRepository receivableRepository, ReceivableFieldFactory fieldFactory,
            ReceivableListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator,
            SalesInvoiceOpenAmountApi salesInvoiceOpenAmountApi) {
        this.receivableRepository = receivableRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
        this.salesInvoiceOpenAmountApi = salesInvoiceOpenAmountApi;
    }

    public ListBaseVO<ReceivableListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<Receivable> list = receivableRepository.findByCondition(conditionMap);
        Long total = receivableRepository.count(conditionMap);
        ListBaseVO<ReceivableListItemVO> vo = new ListBaseVO<>();
        List<ReceivableListItemVO> items = list.stream().map(ReceivableAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.RECEIVABLE.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<ReceivableSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<ReceivableSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.CREATE));
        vo.setFormSections(ReceivableFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        ReceivableSaveItemVO data = ReceivableAdminAssembler.buildEmptySaveItemVO();
        ReceivableMainDTO main = new ReceivableMainDTO();
        main.setReceivableNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.RECEIVABLE.getCode()));
        main.setSourceType(ReceivableSourceTypeEnum.MANUAL_ADJUSTMENT.getCode());
        data.setMain(main);
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<ReceivableSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable entity = receivableRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("应收开放项不存在");
        }
        SaveItemVO<ReceivableSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.UPDATE));
        vo.setFormSections(ReceivableFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        vo.setData(ReceivableAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public ReceivableDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receivable entity = receivableRepository.findById(dto.getCorpid(), dto.getId());
        return ReceivableAdminAssembler.toDetailVO(ReceivableAdminAssembler.toSaveItemVO(entity));
    }

    public SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!"main.sourceInvoiceId".equals(dto.getFieldAttr()) || dto.getReferenceId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售发票回填字段或来源数据不合法");
        }
        SalesInvoiceOpenAmountApi.SalesInvoiceOpenAmount invoice = salesInvoiceOpenAmountApi
            .findOpenAmount(dto.getCorpid(), dto.getReferenceId());
        SettlementSelectionFillVO result = new SettlementSelectionFillVO();
        result.setReferenceId(invoice.invoiceId());
        result.setPatch(Map.of("main.sourceInvoiceId", invoice.invoiceId(), "main.customerId", invoice.customerId()));
        return result;
    }

    public List<ReceivableBusinessSelectOptionVO> businessSelectQuickSearch(ReceivableBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<ReceivableBusinessSelectOptionVO> businessSelectDialogSearch(ReceivableBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        Map<String, Object> conditions = businessSelectConditions(dto, (pageNum - 1) * pageSize, pageSize);
        List<ReceivableBusinessSelectOptionVO> options = receivableRepository.findByCondition(conditions).stream()
            .map(this::toBusinessSelectOption).toList();
        Long total = receivableRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<ReceivableBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public ReceivableBusinessSelectOptionVO businessSelectGetById(ReceivableBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        Receivable receivable = receivableRepository.findById(dto.getCorpid(), dto.getId());
        return receivable == null ? null : toBusinessSelectOption(receivable);
    }

    private List<ReceivableBusinessSelectOptionVO> findBusinessSelectOptions(ReceivableBusinessSelectQueryDTO dto,
                                                                               Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getCustomerId() == null) {
            return List.of();
        }
        return receivableRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(ReceivableBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("customerId", dto.getCustomerId());
        conditions.put("businessSelectOnlyOpen", Boolean.TRUE);
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private ReceivableBusinessSelectOptionVO toBusinessSelectOption(Receivable receivable) {
        ReceivableBusinessSelectOptionVO option = new ReceivableBusinessSelectOptionVO();
        option.setId(receivable.getId());
        option.setCode(receivable.getReceivableNo());
        option.setName("未核销余额 " + receivable.getRemainingAmount());
        option.setLabel(receivable.getReceivableNo() + "（未核销余额：" + receivable.getRemainingAmount() + "）");
        option.setRemainingAmount(receivable.getRemainingAmount());
        return option;
    }

    private List<xbb.ai.erp.base.common.filed.FieldEntity> buildFormHeadList(SceneTypeEnum scene) {
        return SceneFieldAssembler.buildHeadList(fieldFactory.getFields(scene)).stream().map(field -> {
            if ("main.sourceInvoiceId".equals(field.getAttr())) {
                xbb.ai.erp.base.common.filed.FieldEntity.SelectionFillConfig config =
                    new xbb.ai.erp.base.common.filed.FieldEntity.SelectionFillConfig();
                config.setEnabled(Boolean.TRUE);
                field.setSelectionFillConfig(config);
            }
            return field;
        }).toList();
    }

    private static Map<String, Object> buildFormLinkageConfig() {
        Map<String, Object> customerClearPatch = new java.util.HashMap<>();
        customerClearPatch.put("main.sourceInvoiceId", null);
        return Map.of(
            "selectionQueryContexts", List.of(Map.of("fieldAttr", "main.sourceInvoiceId",
                "contextAttr", "main.customerId", "paramName", "customerId")),
            "clearRules", List.of(Map.of("fieldAttr", "main.customerId",
                "patch", customerClearPatch))
        );
    }
}
