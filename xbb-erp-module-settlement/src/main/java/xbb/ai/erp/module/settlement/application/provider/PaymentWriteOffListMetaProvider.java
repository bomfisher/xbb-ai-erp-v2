package xbb.ai.erp.module.settlement.application.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.base.common.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.settlement.admin.PaymentWriteOffFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;

@Component
public class PaymentWriteOffListMetaProvider implements ListMetaProvider {
    @Override
    public String businessCode() {
        return BusinessCodeEnum.PAYMENT_WRITEOFF.getCode();
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
        return Arrays.stream(PaymentWriteOffFieldEnum.values())
            .map(PaymentWriteOffFieldEnum::toSceneFieldMeta)
            .map(SceneFieldAssembler::build)
            .toList();
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo result = new ListMetaBundlePojo();
        result.setTopButtonList(List.of(new ListButtonItemPojo("ADD", "新建", 10, "ADD")));
        return result;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo result = new ListMetaBundlePojo();
        result.setBottomButtonList(List.of());
        return result;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListRowActionItemPojo action = new ListRowActionItemPojo();
        action.setActionCode("REVERSE");
        action.setActionName("冲销核销");
        action.setSort(10);
        action.setShowMode("MORE");
        action.setConfirmType("CONFIRM");
        ListMetaBundlePojo result = new ListMetaBundlePojo();
        result.setRowActionList(List.of(action));
        return result;
    }
}
