package xbb.ai.erp.module.purchase.application.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.base.common.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class PurchaseInvoiceListMetaProvider implements ListMetaProvider {

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PURCHASE_INVOICE.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return Arrays.stream(PurchaseInvoiceFieldEnum.values()).filter(field -> field.getFilterName() != null)
            .map(this::buildFilterField).toList();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        Map<String, ListFilterMetaPojo> metadata = new LinkedHashMap<>();
        Arrays.stream(PurchaseInvoiceFieldEnum.values()).filter(field -> field.getFilterName() != null)
            .forEach(field -> metadata.put(field.getAttr(), new ListFilterMetaPojo(field.getAttr(), field.getFilterName(),
                filterRule(field).protocolFieldType(), filterRule(field).supportedSymbols())));
        return Map.copyOf(metadata);
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return Arrays.stream(PurchaseInvoiceFieldEnum.values()).filter(field -> field.supports(SceneTypeEnum.LIST))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta())).toList();
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(new ListButtonItemPojo("ADD", "新建", 10, "ADD"), new ListButtonItemPojo("AUDIT", "审核/反审核", 20, "AUDIT")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of());
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        List<ListRowActionItemPojo> actions = new ArrayList<>();
        actions.add(rowAction("EDIT", "编辑", 10, "PRIMARY", "NONE"));
        actions.add(rowAction("POST", "过账", 20, "MORE", "CONFIRM"));
        actions.add(rowAction("RED_FLUSH", "红冲", 30, "MORE", "CONFIRM"));
        actions.add(rowAction("VOID", "作废", 40, "MORE", "CONFIRM"));
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(actions);
        return bundle;
    }

    private ListRowActionItemPojo rowAction(String actionCode, String actionName, int sort, String showMode,
                                             String confirmType) {
        ListRowActionItemPojo action = new ListRowActionItemPojo();
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setSort(sort);
        action.setShowMode(showMode);
        action.setConfirmType(confirmType);
        return action;
    }

    private FilterField buildFilterField(PurchaseInvoiceFieldEnum field) {
        FilterField result = new FilterField();
        result.setAttr(field.getAttr());
        result.setAttrName(field.getAttrName());
        result.setSourceFieldType(field.getFieldType().getType());
        result.setFieldType(String.valueOf(field.getFieldType().getType()));
        result.setFilterFieldType(filterRule(field).protocolFieldType());
        result.setSupportedSymbols(filterRule(field).supportedSymbols());
        result.setItemList(field.itemList());
        result.setBusinessSelectConfig(field.businessSelectConfig());
        return result;
    }

    private static ListFilterFieldTypeRule filterRule(PurchaseInvoiceFieldEnum field) {
        return ListFilterFieldTypeRule.find(field.getFieldType().getType())
            .orElseThrow(() -> new IllegalArgumentException("不支持的筛选字段类型: " + field.getFieldType()));
    }
}
