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
public class PurchasePendingTaskListMetaProvider implements ListMetaProvider {

    private static final List<PurchaseListMetaSupport.FilterDefinition> FILTER_DEFINITIONS = List.of(
        new PurchaseListMetaSupport.FilterDefinition("taskNo", "任务号", "TEXT", "task_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("purchaseOrgId", "采购组织", "ID", "purchase_org_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceType", "来源类型", "TEXT", "source_type", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceDocId", "来源单据ID", "ID", "source_doc_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceLineId", "来源单据行ID", "ID", "source_line_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceDocNo", "来源单号", "TEXT", "source_doc_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuId", "物料", "ID", "sku_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuCodeSnapshot", "物料编码", "TEXT", "sku_code_snapshot", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuNameSnapshot", "物料名称", "TEXT", "sku_name_snapshot", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("suggestedVendorId", "建议供应商", "ID", "suggested_vendor_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("suggestedDeliveryDate", "建议交期", "DATE", "suggested_delivery_date", PurchaseListMetaSupport.DATE_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("taskStatus", "任务状态", "TEXT", "task_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of())
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = PurchaseListMetaSupport.buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_PENDING_TASK.getCode();
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
            PurchaseListMetaSupport.buildHeader("main.taskNo", "任务号"),
            PurchaseListMetaSupport.buildHeader("main.purchaseOrgId", "采购组织"),
            PurchaseListMetaSupport.buildHeader("main.sourceType", "来源类型"),
            PurchaseListMetaSupport.buildHeader("main.skuCodeSnapshot", "物料编码"),
            PurchaseListMetaSupport.buildHeader("main.skuNameSnapshot", "物料名称"),
            PurchaseListMetaSupport.buildHeader("main.needQty", "需求数量"),
            PurchaseListMetaSupport.buildHeader("main.occupiedQty", "已占用数量"),
            PurchaseListMetaSupport.buildHeader("main.generatedRequestQty", "已转申请数量"),
            PurchaseListMetaSupport.buildHeader("main.generatedOrderQty", "已转订单数量"),
            PurchaseListMetaSupport.buildHeader("main.closedQty", "已关闭数量"),
            PurchaseListMetaSupport.buildHeader("main.priorityLevel", "优先级"),
            PurchaseListMetaSupport.buildHeader("main.taskStatus", "任务状态"),
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
