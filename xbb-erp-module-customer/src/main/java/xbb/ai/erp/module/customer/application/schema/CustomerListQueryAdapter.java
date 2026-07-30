package xbb.ai.erp.module.customer.application.schema;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomerListQueryAdapter {

    private final CustomerListSchemaProvider customerListSchemaProvider;
    private final ListFilterConditionBuilder listFilterConditionBuilder;

    public CustomerListQueryAdapter(CustomerListSchemaProvider customerListSchemaProvider) {
        this.customerListSchemaProvider = customerListSchemaProvider;
        this.listFilterConditionBuilder = new ListFilterConditionBuilder();
    }

    public Map<String, Object> toConditionMap(CustomerListDTO dto) {
        CustomerQueryPojo queryPojo = toQueryPojo(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", queryPojo.getCorpid());
        conditionMap.put("keyword", queryPojo.getKeyword());
        conditionMap.put("conditions", queryPojo.getConditions());
        return conditionMap;
    }

    public CustomerQueryPojo toQueryPojo(CustomerListDTO dto) {
        ListCommonQueryDTO queryDTO = new ListCommonQueryDTO();
        queryDTO.setCorpid(dto.getCorpid());
        queryDTO.setUserId(dto.getUserId());
        queryDTO.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

        CustomerQueryPojo queryPojo = new CustomerQueryPojo();
        queryPojo.setCorpid(dto.getCorpid());
        queryPojo.setKeyword(dto.getKeyword());
        queryPojo.setConditions(listFilterConditionBuilder.build(dto.getConditions(), customerListSchemaProvider.conditionMetaMap(queryDTO)));
        return queryPojo;
    }
}
