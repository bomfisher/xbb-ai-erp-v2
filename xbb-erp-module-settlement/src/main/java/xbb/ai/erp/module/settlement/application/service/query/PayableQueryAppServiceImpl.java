package xbb.ai.erp.module.settlement.application.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.PayableListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableMainDTO;
import xbb.ai.erp.module.settlement.admin.PayableFieldEnum;
import xbb.ai.erp.module.settlement.admin.vo.PayableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableSaveItemVO;
import xbb.ai.erp.module.settlement.application.assembler.PayableAdminAssembler;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.settlement.domain.model.Payable;
import xbb.ai.erp.module.settlement.domain.repository.PayableRepository;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;
import xbb.ai.erp.module.purchase.contract.PurchaseInvoiceOpenAmountApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayableQueryAppServiceImpl {

    private final PayableRepository payableRepository;
    private final ListValueRenderer listValueRenderer;
    private final PurchaseInvoiceOpenAmountApi purchaseInvoiceOpenAmountApi;
    private final BizNoGenerator bizNoGenerator;

    public PayableQueryAppServiceImpl(PayableRepository payableRepository, ListValueRenderer listValueRenderer,
                                     PurchaseInvoiceOpenAmountApi purchaseInvoiceOpenAmountApi, BizNoGenerator bizNoGenerator) {
        this.payableRepository = payableRepository;
        this.listValueRenderer = listValueRenderer;
        this.purchaseInvoiceOpenAmountApi = purchaseInvoiceOpenAmountApi;
        this.bizNoGenerator = bizNoGenerator;
    }

    public ListBaseVO<PayableListItemVO> list(PayableListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("payableNo", dto.getPayableNo());
        conditionMap.put("supplierId", dto.getSupplierId());
        conditionMap.put("sourceType", dto.getSourceType());
        conditionMap.put("sourceInvoiceId", dto.getSourceInvoiceId());
        conditionMap.put("payableDate", dto.getPayableDate());
        conditionMap.put("dueDate", dto.getDueDate());
        conditionMap.put("amount", dto.getAmount());
        conditionMap.put("writtenOffAmount", dto.getWrittenOffAmount());
        conditionMap.put("remainingAmount", dto.getRemainingAmount());
        conditionMap.put("status", dto.getStatus());
        conditionMap.put("auditStatus", dto.getAuditStatus());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("conditions", dto.getConditions());
        List<Payable> list = payableRepository.findByCondition(conditionMap);
        Long total = payableRepository.count(conditionMap);
        ListBaseVO<PayableListItemVO> vo = new ListBaseVO<>();
        List<PayableListItemVO> items = list.stream().map(PayableAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.PAYABLE.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<PayableSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<PayableSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        PayableSaveItemVO data = PayableAdminAssembler.buildEmptySaveItemVO();
        PayableMainDTO main = new PayableMainDTO();
        main.setPayableNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PAYABLE.getCode()));
        data.setMain(main);
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<PayableSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable entity = payableRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<PayableSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        vo.setData(PayableAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public PayableDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payable entity = payableRepository.findById(dto.getCorpid(), dto.getId());
        return PayableAdminAssembler.toDetailVO(PayableAdminAssembler.toSaveItemVO(entity));
    }

    public SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!"main.sourceInvoiceId".equals(dto.getFieldAttr()) || dto.getReferenceId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("采购发票回填字段或来源数据不合法");
        }
        PurchaseInvoiceOpenAmountApi.PurchaseInvoiceOpenAmount invoice = purchaseInvoiceOpenAmountApi
            .findOpenAmount(dto.getCorpid(), dto.getReferenceId());
        SettlementSelectionFillVO result = new SettlementSelectionFillVO();
        result.setReferenceId(invoice.invoiceId());
        result.setPatch(Map.of(
            "main.sourceInvoiceId", invoice.invoiceId(),
            "main.supplierId", invoice.supplierId(),
            "main.amount", invoice.availableAmount()
        ));
        return result;
    }

    private List<xbb.ai.erp.base.common.filed.FieldEntity> buildFormHeadList(SceneTypeEnum scene) {
        return java.util.Arrays.stream(PayableFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .map(field -> {
                if ("main.sourceInvoiceId".equals(field.getAttr())) {
                    xbb.ai.erp.base.common.filed.FieldEntity.SelectionFillConfig config =
                        new xbb.ai.erp.base.common.filed.FieldEntity.SelectionFillConfig();
                    config.setEnabled(Boolean.TRUE);
                    field.setSelectionFillConfig(config);
                }
                return field;
            })
            .toList();
    }

    private static Map<String, Object> buildFormLinkageConfig() {
        Map<String, Object> supplierClearPatch = new HashMap<>();
        supplierClearPatch.put("main.sourceInvoiceId", null);
        return Map.of(
            "selectionQueryContexts", List.of(Map.of("fieldAttr", "main.sourceInvoiceId",
                "contextAttr", "main.supplierId", "paramName", "supplierId")),
            "clearRules", List.of(Map.of("fieldAttr", "main.supplierId",
                "patch", supplierClearPatch))
        );
    }
}
