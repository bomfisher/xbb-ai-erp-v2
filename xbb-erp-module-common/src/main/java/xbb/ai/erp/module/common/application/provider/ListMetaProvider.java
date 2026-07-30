package xbb.ai.erp.module.common.application.provider;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaContextPojo;

import java.util.List;
import java.util.Map;

public interface ListMetaProvider {

    String businessCode();

    List<FilterField> buildFilterMeta(ListCommonQueryDTO dto);

    Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto);

    List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto);

    ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto);

    ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto);

    ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto);

    default void applyPackageExtension(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }

    default void applyPermissionTrim(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }
}
