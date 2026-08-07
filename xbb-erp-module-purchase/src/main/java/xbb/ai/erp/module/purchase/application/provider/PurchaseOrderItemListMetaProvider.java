package xbb.ai.erp.module.purchase.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import java.util.List;
import java.util.Map;

@Component
public class PurchaseOrderItemListMetaProvider implements ListMetaProvider {

    private static final List<FieldItem> GIFT_FLAG_ITEMS = buildGiftFlagItems();
    private static final List<PurchaseListMetaSupport.FilterDefinition> FILTER_DEFINITIONS = List.of(
        new PurchaseListMetaSupport.FilterDefinition("orderId", "采购订单ID", "ID", "order_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("lineNo", "行号", "TEXT", "line_no", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuId", "物料", "ID", "sku_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuCodeSnapshot", "物料编码", "TEXT", "sku_code_snapshot", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("skuNameSnapshot", "物料名称", "TEXT", "sku_name_snapshot", PurchaseListMetaSupport.TEXT_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("purchaseUnitId", "采购单位", "ID", "purchase_unit_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("warehouseId", "仓库", "ID", "warehouse_id", PurchaseListMetaSupport.ID_SYMBOLS, List.of()),
        new PurchaseListMetaSupport.FilterDefinition("isGift", "是否赠品", "ENUM", "is_gift", PurchaseListMetaSupport.ENUM_SYMBOLS, GIFT_FLAG_ITEMS)
    );
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = PurchaseListMetaSupport.buildConditionMetaMap(FILTER_DEFINITIONS);

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_ORDER_ITEM.getCode();
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
            PurchaseListMetaSupport.buildHeader("main.orderId", "采购订单ID"),
            PurchaseListMetaSupport.buildHeader("main.lineNo", "行号"),
            PurchaseListMetaSupport.buildHeader("main.skuCodeSnapshot", "物料编码"),
            PurchaseListMetaSupport.buildHeader("main.skuNameSnapshot", "物料名称"),
            PurchaseListMetaSupport.buildHeader("main.orderQty", "订单数量"),
            PurchaseListMetaSupport.buildHeader("main.receivedQty", "已收数量"),
            PurchaseListMetaSupport.buildHeader("main.inboundedQty", "已入库数量"),
            PurchaseListMetaSupport.buildHeader("main.closedQty", "已关闭数量"),
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

    private static List<FieldItem> buildGiftFlagItems() {
        FieldItem no = new FieldItem();
        no.setValue("0");
        no.setText("否");
        FieldItem yes = new FieldItem();
        yes.setValue("1");
        yes.setText("是");
        return List.of(no, yes);
    }
}
