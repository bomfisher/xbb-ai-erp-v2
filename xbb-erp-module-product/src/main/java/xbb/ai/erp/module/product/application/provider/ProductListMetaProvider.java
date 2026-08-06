package xbb.ai.erp.module.product.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

import java.util.List;
import java.util.Map;

@Component
public class ProductListMetaProvider implements ListMetaProvider {

    @Override
    public String businessCode() {
        return BusinessCodeEnum.PRODUCT.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return List.of();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return Map.of();
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return List.of(
            buildHeader("main.spuCode", "商品编码"),
            buildHeader("main.spuName", "商品名称"),
            buildHeader("main.productType", "商品类型"),
            buildHeader("main.spuEnableStatus", "主档启用状态")
        );
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(buildButton("ADD", "新增", 10, "ADD")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of(buildButton("EXPORT", "导出", 20, "EXPORT")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
        return bundle;
    }

    private FieldEntity buildHeader(String attr, String attrName) {
        FieldEntity field = new FieldEntity();
        field.setAttr(attr);
        field.setAttrName(attrName);
        field.setFieldType("1");
        field.setRequired(0);
        field.setEditable(1);
        field.setItemList(List.of());
        return field;
    }

    private ListButtonItemPojo buildButton(String buttonCode, String buttonName, Integer sort, String actionCode) {
        ListButtonItemPojo item = new ListButtonItemPojo();
        item.setButtonCode(buttonCode);
        item.setButtonName(buttonName);
        item.setSort(sort);
        item.setActionCode(actionCode);
        return item;
    }

    private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
        ListRowActionItemPojo item = new ListRowActionItemPojo();
        item.setActionCode(actionCode);
        item.setActionName(actionName);
        item.setSort(sort);
        item.setShowMode(showMode);
        item.setConfirmType(confirmType);
        return item;
    }
}
