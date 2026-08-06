package xbb.ai.erp.module.supplier.application.schema;

import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.application.provider.SupplierListMetaProvider;
import xbb.ai.erp.module.supplier.domain.pojo.SupplierQueryPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;

import java.util.Map;

public class SupplierListQueryAdapter {

    private final ListFilterConditionBuilder listFilterConditionBuilder = new ListFilterConditionBuilder();

    public SupplierQueryPojo toQueryPojo(SupplierListDTO dto) {
        SupplierQueryPojo queryPojo = new SupplierQueryPojo();
        queryPojo.setCorpid(dto.getCorpid());
        queryPojo.setId(dto.getId());
        queryPojo.setKeyword(dto.getKeyword());
        queryPojo.setSupplierCode(dto.getSupplierCode());
        queryPojo.setSupplierName(dto.getSupplierName());
        queryPojo.setSupplierShortName(dto.getSupplierShortName());
        queryPojo.setSupplierCategory(dto.getSupplierCategory());
        queryPojo.setMainBusinessCategory(dto.getMainBusinessCategory());
        queryPojo.setOwnerPurchaserId(dto.getOwnerPurchaserId());
        queryPojo.setBizStatus(dto.getBizStatus());
        queryPojo.setRefStatus(dto.getRefStatus());
        queryPojo.setPageNum(dto.getPageNum());
        queryPojo.setPageSize(dto.getPageSize());
        queryPojo.setOffset(dto.getOffset());
        queryPojo.setGroupByStr(dto.getGroupByStr());
        queryPojo.setOrderByStr(dto.getOrderByStr());
        queryPojo.setConditions(listFilterConditionBuilder.build(dto.getConditions(), SupplierListMetaProvider.conditionMetaMap()));
        return queryPojo;
    }

    public Map<String, Object> toConditionMap(SupplierListDTO dto) {
        SupplierQueryPojo queryPojo = toQueryPojo(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", queryPojo.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", queryPojo.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "keyword", queryPojo.getKeyword());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "supplierCode", queryPojo.getSupplierCode());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "supplierName", queryPojo.getSupplierName());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "supplierShortName", queryPojo.getSupplierShortName());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "supplierCategory", queryPojo.getSupplierCategory());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "mainBusinessCategory", queryPojo.getMainBusinessCategory());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "ownerPurchaserId", queryPojo.getOwnerPurchaserId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", queryPojo.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "refStatus", queryPojo.getRefStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", queryPojo.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", queryPojo.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", queryPojo.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", queryPojo.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", queryPojo.getOrderByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "conditions", queryPojo.getConditions());
        return conditionMap;
    }
}
