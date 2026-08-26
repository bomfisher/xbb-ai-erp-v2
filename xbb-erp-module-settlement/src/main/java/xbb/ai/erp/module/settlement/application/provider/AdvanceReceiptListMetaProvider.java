package xbb.ai.erp.module.settlement.application.provider;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;

@Component
@RequiredArgsConstructor
public class AdvanceReceiptListMetaProvider implements ListMetaProvider {

    private final ReceiptListMetaProvider receiptListMetaProvider;

    @Override
    public String businessCode() {
        return BusinessCodeEnum.ADVANCE_RECEIPT.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildFilterMeta(dto);
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildFilterConditionMeta(dto);
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildHeaderMeta(dto);
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildTopButtonMeta(dto);
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildBottomButtonMeta(dto);
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        return receiptListMetaProvider.buildRowActionMeta(dto);
    }
}
