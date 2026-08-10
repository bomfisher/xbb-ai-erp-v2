package xbb.ai.erp.module.common.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
import xbb.ai.erp.module.common.admin.vo.ListSchemaVO;
import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
import xbb.ai.erp.module.common.application.service.ListCommonService;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListCommonServiceImpl implements ListCommonService {

    private final ListMetaRegistry listMetaRegistry;

    @Override
    public ListFilterVO filter(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListFilterVO vo = new ListFilterVO();
        List<FilterField> fields = provider.buildFilterMeta(dto);
        vo.setList(Objects.isNull(fields) ? Collections.emptyList() : fields.stream()
            .map(this::normalizeFilterField)
            .toList());
        return vo;
    }

    private FilterField normalizeFilterField(FilterField field) {
        if (field.sourceFieldType() == null) {
            if (field.getFilterFieldType() == null) {
                field.setFilterFieldType(field.getFieldType());
            }
            return field;
        }
        ListFilterFieldTypeRule.find(field.sourceFieldType()).ifPresent(rule -> {
            field.setFilterFieldType(rule.protocolFieldType());
            field.setFieldType(String.valueOf(field.sourceFieldType()));
            field.setSupportedSymbols(rule.supportedSymbols());
        });
        if (Objects.equals(FieldTypeEnum.SWITCH.getType(), field.sourceFieldType())) {
            field.setItemList(List.of(fieldItem("1", "开启"), fieldItem("2", "关闭")));
        }
        return field;
    }

    private FieldItem fieldItem(String value, String text) {
        FieldItem item = new FieldItem();
        item.setValue(value);
        item.setText(text);
        return item;
    }

    @Override
    public ListHeaderVO header(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListHeaderVO vo = new ListHeaderVO();
        List<FieldEntity> list = provider.buildHeaderMeta(dto);
        vo.setList(Objects.isNull(list) ? Collections.emptyList() : list);
        return vo;
    }

    @Override
    public ListTopButtonVO topButton(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListMetaBundlePojo bundle = provider.buildTopButtonMeta(dto);
        ListTopButtonVO vo = new ListTopButtonVO();
        vo.setList(Objects.isNull(bundle.getTopButtonList()) ? Collections.emptyList() : bundle.getTopButtonList());
        return vo;
    }

    @Override
    public ListBottomButtonVO bottomButton(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListMetaBundlePojo bundle = provider.buildBottomButtonMeta(dto);
        ListBottomButtonVO vo = new ListBottomButtonVO();
        vo.setList(Objects.isNull(bundle.getBottomButtonList()) ? Collections.emptyList() : bundle.getBottomButtonList());
        return vo;
    }

    @Override
    public ListRowActionVO rowAction(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListMetaBundlePojo bundle = provider.buildRowActionMeta(dto);
        ListRowActionVO vo = new ListRowActionVO();
        vo.setList(Objects.isNull(bundle.getRowActionList()) ? Collections.emptyList() : bundle.getRowActionList());
        return vo;
    }

    @Override
    public ListSchemaVO schema(ListCommonQueryDTO dto) {
        ListSchemaVO vo = new ListSchemaVO();
        vo.setFilter(filter(dto));
        vo.setHeader(header(dto));
        vo.setTopButton(topButton(dto));
        vo.setBottomButton(bottomButton(dto));
        vo.setRowAction(rowAction(dto));
        return vo;
    }
}
