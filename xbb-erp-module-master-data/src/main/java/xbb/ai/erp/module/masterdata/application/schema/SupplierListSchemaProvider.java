package xbb.ai.erp.module.masterdata.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.masterdata.application.provider.SupplierListMetaProvider;

@Component
@RequiredArgsConstructor
public class SupplierListSchemaProvider {
    private final SupplierListMetaProvider listMetaProvider;
    public Map<String, ListFilterMetaPojo> conditionMetaMap() { return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO()); }
}
