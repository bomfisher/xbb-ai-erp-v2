package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.Warehouse;
import xbb.ai.erp.module.product.domain.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryWarehouseRepository implements WarehouseRepository {

    private final List<Warehouse> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Warehouse> all() {
        return data;
    }

    public void seed(Warehouse warehouse) {
        data.add(warehouse);
        sequence.updateAndGet(current -> Math.max(current, warehouse.getId() == null ? current : warehouse.getId() + 1));
    }

    @Override
    public void insert(Warehouse warehouse) {
        if (warehouse.getId() == null) {
            warehouse.setId(sequence.getAndIncrement());
        }
        data.add(warehouse);
    }

    @Override
    public void insertBatch(List<Warehouse> warehouseList) {
        warehouseList.forEach(this::insert);
    }

    @Override
    public void removeById(String corpid, Long id) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && ids.contains(item.getId()));
    }

    @Override
    public void update(Warehouse warehouse) {
        removeById(warehouse.getCorpid(), warehouse.getId());
        data.add(warehouse);
    }

    @Override
    public Warehouse findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Warehouse> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object id = conditionMap.get("id");
        Object bizOrgId = conditionMap.get("bizOrgId");
        Object warehouseCode = conditionMap.get("warehouseCode");
        Object warehouseName = conditionMap.get("warehouseName");
        Object warehouseType = conditionMap.get("warehouseType");
        Object enableStatus = conditionMap.get("enableStatus");
        Object address = conditionMap.get("address");
        Object managerId = conditionMap.get("managerId");
        Object bizStatus = conditionMap.get("bizStatus");
        Object offset = conditionMap.get("offset");
        Object pageSize = conditionMap.get("pageSize");
        List<Warehouse> filtered = data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (id == null || id.equals(item.getId()))
                && (bizOrgId == null || bizOrgId.equals(item.getBizOrgId()))
                && (warehouseCode == null || warehouseCode.equals(item.getWarehouseCode()))
                && (warehouseName == null || warehouseName.equals(item.getWarehouseName()))
                && (warehouseType == null || warehouseType.equals(item.getWarehouseType()))
                && (enableStatus == null || enableStatus.equals(item.getEnableStatus()))
                && (address == null || address.equals(item.getAddress()))
                && (managerId == null || managerId.equals(item.getManagerId()))
                && (bizStatus == null || bizStatus.equals(item.getBizStatus()))
        ).toList();
        if (pageSize instanceof Integer size && size > 0) {
            int start = offset instanceof Integer integerOffset ? Math.max(integerOffset, 0) : 0;
            if (start >= filtered.size()) {
                return List.of();
            }
            int end = Math.min(start + size, filtered.size());
            return filtered.subList(start, end);
        }
        return filtered;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.stream().filter(item ->
            (conditionMap.get("corpid") == null || conditionMap.get("corpid").equals(item.getCorpid()))
                && (conditionMap.get("id") == null || conditionMap.get("id").equals(item.getId()))
                && (conditionMap.get("bizOrgId") == null || conditionMap.get("bizOrgId").equals(item.getBizOrgId()))
                && (conditionMap.get("warehouseCode") == null || conditionMap.get("warehouseCode").equals(item.getWarehouseCode()))
                && (conditionMap.get("warehouseName") == null || conditionMap.get("warehouseName").equals(item.getWarehouseName()))
                && (conditionMap.get("warehouseType") == null || conditionMap.get("warehouseType").equals(item.getWarehouseType()))
                && (conditionMap.get("enableStatus") == null || conditionMap.get("enableStatus").equals(item.getEnableStatus()))
                && (conditionMap.get("address") == null || conditionMap.get("address").equals(item.getAddress()))
                && (conditionMap.get("managerId") == null || conditionMap.get("managerId").equals(item.getManagerId()))
                && (conditionMap.get("bizStatus") == null || conditionMap.get("bizStatus").equals(item.getBizStatus()))
        ).count();
    }
}
