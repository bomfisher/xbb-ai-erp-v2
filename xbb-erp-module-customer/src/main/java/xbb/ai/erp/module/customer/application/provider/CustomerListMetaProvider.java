package xbb.ai.erp.module.customer.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.List;

@Component
public class CustomerListMetaProvider implements ListMetaProvider {

    private final CustomerFieldFactory customerFieldFactory;

    public CustomerListMetaProvider() {
        this(new DefaultCustomerFieldFactory(List.of()));
    }

    public CustomerListMetaProvider(CustomerFieldFactory customerFieldFactory) {
        this.customerFieldFactory = customerFieldFactory;
    }

    @Override
    public String businessCode() {
        return BusinessCodeEnum.CUSTOMER.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return customerFieldFactory.getFields(SceneTypeEnum.LIST).stream()
            .map(this::toFilterField)
            .toList();
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return CustomerFieldAssembler.buildHeadList(customerFieldFactory.getFields(SceneTypeEnum.LIST));
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

    private FilterField toFilterField(SceneFieldMeta fieldMeta) {
        FilterField field = new FilterField();
        field.setAttr(fieldMeta.getAttr());
        field.setAttrName(fieldMeta.getAttrName());
        field.setFieldType(fieldMeta.getFieldType());
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
}
