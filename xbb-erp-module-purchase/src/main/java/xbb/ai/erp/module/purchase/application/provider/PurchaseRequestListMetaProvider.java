package xbb.ai.erp.module.purchase.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import java.util.List;
import java.util.Map;

@Component
public class PurchaseRequestListMetaProvider implements ListMetaProvider {

    private static final List<PurchaseListMetaSupport.FilterDefinition> FILTER_DEFINITIONS = List.of(
        new PurchaseListMetaSupport.FilterDefinition("requestNo", "采购申请单号", "TEXT", "request_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("purchaseOrgId", "采购组织", "ID", "purchase_org_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("requestDeptId", "申请部门", "ID", "request_dept_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("applicantId", "申请人", "ID", "applicant_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceType", "来源类型", "TEXT", "source_type", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceNo", "来源单号", "TEXT", "source_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("suggestedVendorId", "建议供应商", "ID", "suggested_vendor_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("suggestedDeliveryDate", "建议交期", "DATE", "suggested_delivery_date", PurchaseListMetaSupport.DATE_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("bizStatus", "业务状态", "TEXT", "biz_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("approvalStatus", "审批状态", "TEXT", "approval_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of())
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = PurchaseListMetaSupport.buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_REQUEST.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return FILTER_DEFINITIONS.stream().map(PurchaseListMetaSupport::buildFilterField).toList();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return CONDITION_META_MAP;
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return List.of(
            PurchaseListMetaSupport.buildHeader("main.requestNo", "采购申请单号"),
            PurchaseListMetaSupport.buildHeader("main.purchaseOrgId", "采购组织"),
            PurchaseListMetaSupport.buildHeader("main.sourceType", "来源类型"),
            PurchaseListMetaSupport.buildHeader("main.bizStatus", "业务状态"),
            PurchaseListMetaSupport.buildHeader("main.approvalStatus", "审批状态"),
            PurchaseListMetaSupport.buildHeader("main.grossAmount", "含税金额"),
            PurchaseListMetaSupport.buildHeader("main.netAmount", "未税金额"),
            PurchaseListMetaSupport.buildHeader("main.taxAmount", "税额"),
            PurchaseListMetaSupport.buildHeader("main.addTime", "创建时间"),
            PurchaseListMetaSupport.buildHeader("main.updateTime", "更新时间")
        );
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(PurchaseListMetaSupport.buildButton("ADD", "新增", 10, "ADD")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of(PurchaseListMetaSupport.buildButton("EXPORT", "导出", 20, "EXPORT")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(List.of(PurchaseListMetaSupport.buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
        return bundle;
    }
}
