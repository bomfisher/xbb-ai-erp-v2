package xbb.ai.erp.module.customer.application.schema;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.customer.application.provider.CustomerListMetaProvider;

import java.util.Map;

@Component
public class CustomerListSchemaProvider {

    public Map<String, ListFilterMetaPojo> conditionMetaMap(ListCommonQueryDTO dto) {
        return CustomerListMetaProvider.conditionMetaMap();
    }
}
