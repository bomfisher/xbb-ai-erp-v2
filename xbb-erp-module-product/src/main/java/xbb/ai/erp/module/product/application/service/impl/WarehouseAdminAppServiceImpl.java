package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.WarehouseListDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.product.application.assembler.WarehouseAdminAssembler;
import xbb.ai.erp.module.product.application.service.WarehouseAdminAppService;
import xbb.ai.erp.module.product.application.support.WarehouseFieldEnum;
import xbb.ai.erp.module.product.domain.model.Warehouse;
import xbb.ai.erp.module.product.domain.repository.WarehouseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseAdminAppServiceImpl implements WarehouseAdminAppService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseAdminAppServiceImpl() {
        this(null);
    }

    public WarehouseAdminAppServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public static WarehouseAdminAppServiceImpl forTesting(WarehouseRepository warehouseRepository) {
        return new WarehouseAdminAppServiceImpl(warehouseRepository);
    }

    @Override
    public ListBaseVO<WarehouseListItemVO> list(WarehouseListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("bizOrgId", dto.getBizOrgId());
        conditionMap.put("warehouseCode", dto.getWarehouseCode());
        conditionMap.put("warehouseName", dto.getWarehouseName());
        conditionMap.put("warehouseType", dto.getWarehouseType());
        conditionMap.put("enableStatus", dto.getEnableStatus());
        conditionMap.put("address", dto.getAddress());
        conditionMap.put("managerId", dto.getManagerId());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<Warehouse> list = warehouseRepository == null ? List.of() : warehouseRepository.findByCondition(conditionMap);
        Long total = warehouseRepository == null ? 0L : warehouseRepository.count(conditionMap);
        ListBaseVO<WarehouseListItemVO> vo = new ListBaseVO<>();
        vo.setHeadList(WarehouseFieldEnum.listHead());
        vo.setList(list.stream().map(WarehouseAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<WarehouseSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<WarehouseSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(WarehouseFieldEnum.formHead());
        vo.setData(WarehouseAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<WarehouseSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<WarehouseSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(WarehouseFieldEnum.formHead());
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(WarehouseSaveDTO dto) {
        Warehouse warehouse = WarehouseAdminAssembler.toWarehouse(dto);
        long now = System.currentTimeMillis();
        if (warehouse.getId() == null) {
            warehouse.setCreatorId(dto.getUserId());
            warehouse.setModifyId(dto.getUserId());
            warehouse.setDeleted(0);
            warehouse.setAddTime(now);
            warehouse.setUpdateTime(now);
            warehouseRepository.insert(warehouse);
        } else {
            Warehouse existed = warehouseRepository.findById(dto.getCorpid(), warehouse.getId());
            if (existed != null) {
                if (warehouse.getBizOrgId() == null) {
                    warehouse.setBizOrgId(existed.getBizOrgId());
                }
                if (warehouse.getWarehouseName() == null) {
                    warehouse.setWarehouseName(existed.getWarehouseName());
                }
                if (warehouse.getWarehouseCode() == null) {
                    warehouse.setWarehouseCode(existed.getWarehouseCode());
                }
                if (warehouse.getWarehouseType() == null) {
                    warehouse.setWarehouseType(existed.getWarehouseType());
                }
                if (warehouse.getEnableStatus() == null) {
                    warehouse.setEnableStatus(existed.getEnableStatus());
                }
                if (warehouse.getAddress() == null) {
                    warehouse.setAddress(existed.getAddress());
                }
                if (warehouse.getManagerId() == null) {
                    warehouse.setManagerId(existed.getManagerId());
                }
                if (warehouse.getBizStatus() == null) {
                    warehouse.setBizStatus(existed.getBizStatus());
                }
                warehouse.setCreatorId(existed.getCreatorId());
                warehouse.setDeleted(existed.getDeleted());
                warehouse.setAddTime(existed.getAddTime());
            }
            warehouse.setModifyId(dto.getUserId());
            warehouse.setUpdateTime(now);
            warehouseRepository.update(warehouse);
        }
        return warehouse.getId();
    }

    @Override
    public WarehouseDetailVO detail(IdBaseDTO dto) {
        WarehouseDetailVO detailVO = WarehouseAdminAssembler.toDetailVO(toSaveItem(dto));
        detailVO.setHeadList(WarehouseFieldEnum.formHead());
        return detailVO;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty() || warehouseRepository == null) {
            return;
        }
        warehouseRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private WarehouseSaveItemVO toSaveItem(IdBaseDTO dto) {
        if (warehouseRepository == null) {
            return WarehouseAdminAssembler.buildEmptySaveItemVO();
        }
        return WarehouseAdminAssembler.toSaveItemVO(warehouseRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
