package xbb.ai.erp.module.purchase.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import java.util.List;
import java.util.Map;

@Component
public class PurchaseOrderListMetaProvider implements ListMetaProvider {

    private static final List<PurchaseListMetaSupport.FilterDefinition> FILTER_DEFINITIONS = List.of(
        new PurchaseListMetaSupport.FilterDefinition("orderNo", "采购订单号", "TEXT", "order_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("purchaseOrgId", "采购组织", "ID", "purchase_org_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("vendorId", "供应商", "ID", "vendor_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("vendorNameSnapshot", "供应商名称", "TEXT", "vendor_name_snapshot", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("purchaserId", "采购员", "ID", "purchaser_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("warehouseId", "仓库", "ID", "warehouse_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("currencyCode", "币种", "TEXT", "currency_code", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("deliveryDate", "交期", "DATE", "delivery_date", PurchaseListMetaSupport.DATE_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceType", "来源类型", "TEXT", "source_type", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("sourceNo", "来源单号", "TEXT", "source_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("bizStatus", "业务状态", "TEXT", "biz_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("approvalStatus", "审批状态", "TEXT", "approval_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("executionStatus", "执行状态", "TEXT", "execution_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("receiptStatus", "收料状态", "TEXT", "receipt_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("inboundStatus", "入库状态", "TEXT", "inbound_status", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of())
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = PurchaseListMetaSupport.buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_ORDER.getCode();
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
            PurchaseListMetaSupport.buildHeader("main.orderNo", "采购订单号"),
            PurchaseListMetaSupport.buildHeader("main.purchaseOrgId", "采购组织"),
            PurchaseListMetaSupport.buildHeader("main.vendorNameSnapshot", "供应商名称"),
            PurchaseListMetaSupport.buildHeader("main.purchaserNameSnapshot", "采购员名称"),
            PurchaseListMetaSupport.buildHeader("main.warehouseNameSnapshot", "仓库名称"),
            PurchaseListMetaSupport.buildHeader("main.currencyCode", "币种"),
            PurchaseListMetaSupport.buildHeader("main.deliveryDate", "交期"),
            PurchaseListMetaSupport.buildHeader("main.bizStatus", "业务状态"),
            PurchaseListMetaSupport.buildHeader("main.approvalStatus", "审批状态"),
            PurchaseListMetaSupport.buildHeader("main.executionStatus", "执行状态"),
            PurchaseListMetaSupport.buildHeader("main.receiptStatus", "收料状态"),
            PurchaseListMetaSupport.buildHeader("main.inboundStatus", "入库状态"),
            PurchaseListMetaSupport.buildHeader("main.grossAmount", "含税金额"),
            PurchaseListMetaSupport.buildHeader("main.netAmount", "未税金额"),
            PurchaseListMetaSupport.buildHeader("main.taxAmount", "税额"),
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
