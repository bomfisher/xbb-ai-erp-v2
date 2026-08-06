package xbb.ai.erp.module.purchase.application.service.query;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.application.assembler.ProductAdminAssembler;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseRequestAdminAssembler;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.List;
import java.util.Map;

public class PurchaseRequestQueryAppServiceImpl implements PurchaseRequestQueryAppService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    public PurchaseRequestQueryAppServiceImpl(
        PurchaseRequestRepository purchaseRequestRepository,
        PurchaseRequestItemRepository purchaseRequestItemRepository
    ) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestItemRepository = purchaseRequestItemRepository;
    }

    @Override
    public ListBaseVO<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseOrgId", dto.getPurchaseOrgId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "requestNo", dto.getRequestNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "requestDeptId", dto.getRequestDeptId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "applicantId", dto.getApplicantId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceType", dto.getSourceType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceNo", dto.getSourceNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedVendorId", dto.getSuggestedVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", dto.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "approvalStatus", dto.getApprovalStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        List<PurchaseRequest> list = purchaseRequestRepository.findByCondition(conditionMap);
        Long total = purchaseRequestRepository.count(conditionMap);
        ListBaseVO<PurchaseRequestListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseRequestAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseRequestSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildHeadList(dto == null ? null : dto.getCorpid()));
        vo.setData(PurchaseRequestAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseRequestSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildHeadList(dto == null ? null : dto.getCorpid()));
        vo.setData(loadSaveItem(dto));
        return vo;
    }

    @Override
    public PurchaseRequestDetailVO detail(IdBaseDTO dto) {
        return PurchaseRequestAdminAssembler.toDetailVO(loadSaveItem(dto));
    }

    private PurchaseRequestSaveItemVO loadSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseRequest request = purchaseRequestRepository.findById(dto.getCorpid(), dto.getId());
        Map<String, Object> itemConditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(itemConditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(itemConditionMap, "requestId", dto.getId());
        List<PurchaseRequestItem> items = purchaseRequestItemRepository == null ? List.of() : purchaseRequestItemRepository.findByCondition(itemConditionMap);
        return PurchaseRequestAdminAssembler.toSaveItemVO(request, items);
    }

    private List<FieldEntity> buildHeadList(String corpid) {
        return List.of(
            field("main.purchaseOrgId", "采购组织", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.requestNo", "采购申请单号", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("main.requestDeptId", "申请部门", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.applicantId", "申请人", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.sourceType", "来源类型", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.sourceNo", "来源单号", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.suggestedVendorId", "建议供应商", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.suggestedDeliveryDate", "建议交期", FieldTypeEnum.DATE.getType(), 0, 1),
            field("main.bizStatus", "业务状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.approvalStatus", "审批状态", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("main.grossAmount", "估算含税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.netAmount", "估算未税金额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.taxAmount", "估算税额", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("main.remark", "备注", FieldTypeEnum.TEXT.getType(), 0, 1),
            itemContainer("items", "产品明细", corpid)
        );
    }

    private FieldEntity itemContainer(String attr, String attrName, String corpid) {
        FieldEntity entity = field(attr, attrName, FieldTypeEnum.PRODUCT.getType(), 1, 1);
        entity.setSubField(List.of(
            field("lineNo", "行号", FieldTypeEnum.NUM_INT.getType(), 1, 1),
            productSelectField("skuId", "产品", 1, corpid),
            field("skuCodeSnapshot", "SKU编码快照", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("skuNameSnapshot", "SKU名称快照", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("specSnapshot", "规格快照", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("purchaseUnitId", "采购单位ID", FieldTypeEnum.TEXT.getType(), 1, 1),
            field("requestQty", "申请数量", FieldTypeEnum.AMOUNT.getType(), 1, 1),
            field("reservedQty", "已占用下推量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("executedQty", "已正式下推量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("closedQty", "已关闭量", FieldTypeEnum.AMOUNT.getType(), 0, 1),
            field("suggestedVendorId", "建议供应商", FieldTypeEnum.TEXT.getType(), 0, 1),
            field("suggestedDeliveryDate", "建议交期", FieldTypeEnum.DATE.getType(), 0, 1)
        ));
        return entity;
    }

    private FieldEntity productSelectField(String attr, String attrName, Integer required, String corpid) {
        FieldEntity entity = field(attr, attrName, FieldTypeEnum.USER.getType(), required, 1);
        entity.setBusinessSelectConfig(ProductAdminAssembler.buildProductBusinessSelectConfig(corpid, "PURCHASE_REQUEST"));
        return entity;
    }

    private FieldEntity field(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        FieldEntity entity = new FieldEntity();
        entity.setAttr(attr);
        entity.setAttrName(attrName);
        entity.setFieldType(String.valueOf(fieldType));
        entity.setRequired(required);
        entity.setEditable(editable);
        entity.setItemList(List.of());
        return entity;
    }
}
