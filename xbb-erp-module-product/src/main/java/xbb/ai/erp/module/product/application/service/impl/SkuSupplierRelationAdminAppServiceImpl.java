package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO;
import xbb.ai.erp.module.product.application.assembler.SkuSupplierRelationAdminAssembler;
import xbb.ai.erp.module.product.application.service.SkuSupplierRelationAdminAppService;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationListItem;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSkuSupplierRelationRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SkuSupplierRelationAdminAppServiceImpl implements SkuSupplierRelationAdminAppService {

    private final ProductSkuSupplierRelationRepository relationRepository;
    private final ProductSkuRepository productSkuRepository;
    private final SupplierRepository supplierRepository;

    public SkuSupplierRelationAdminAppServiceImpl(
        ProductSkuSupplierRelationRepository relationRepository,
        ProductSkuRepository productSkuRepository,
        SupplierRepository supplierRepository
    ) {
        this.relationRepository = relationRepository;
        this.productSkuRepository = productSkuRepository;
        this.supplierRepository = supplierRepository;
    }

    public static SkuSupplierRelationAdminAppServiceImpl forTesting(
        ProductSkuSupplierRelationRepository relationRepository,
        ProductSkuRepository productSkuRepository,
        SupplierRepository supplierRepository
    ) {
        return new SkuSupplierRelationAdminAppServiceImpl(relationRepository, productSkuRepository, supplierRepository);
    }

    @Override
    public ListBaseVO<SkuSupplierRelationListItemVO> list(SkuSupplierRelationListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "view", dto.getView());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "spuId", dto.getSpuId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "supplierId", dto.getSupplierId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        conditionMap = QueryConditionMapHelper.prepare(conditionMap);
        List<ProductSkuSupplierRelationListItem> list = relationRepository == null ? List.of() : relationRepository.findListByCondition(conditionMap);
        long total = relationRepository == null ? 0L : relationRepository.countListByCondition(conditionMap);
        ListBaseVO<SkuSupplierRelationListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(SkuSupplierRelationAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), (int) total));
        return vo;
    }

    @Override
    public SaveItemVO<SkuSupplierRelationMainDTO> addItem(SkuSupplierRelationQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        List<ProductSkuSupplierRelationSkuOption> skuOptions = resolveSkuOptions(dto.getCorpid(), dto.getSpuId());
        List<ProductSkuSupplierRelationSupplierOption> supplierOptions = resolveSupplierOptions(dto.getCorpid(), dto.getSupplierId());
        SkuSupplierRelationMainDTO main = SkuSupplierRelationAdminAssembler.buildEmptyMain();
        if (dto.getSupplierId() != null) {
            main.setSupplierId(dto.getSupplierId());
        }
        if (dto.getSpuId() != null && skuOptions.size() == 1) {
            main.setSkuId(skuOptions.get(0).getSkuId());
        }
        SaveItemVO<SkuSupplierRelationMainDTO> result = SkuSupplierRelationAdminAssembler.buildSaveItemVO(main, skuOptions, dto.getCorpid());
        return result;
    }

    @Override
    public SaveItemVO<SkuSupplierRelationMainDTO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSkuSupplierRelation relation = requireRelation(dto.getCorpid(), dto.getId());
        ProductSkuSupplierRelationSkuOption currentSkuOption = relationRepository == null ? null : relationRepository.findSkuOptionById(dto.getCorpid(), relation.getSkuId());
        ProductSku currentSku = productSkuRepository == null ? null : productSkuRepository.findById(dto.getCorpid(), relation.getSkuId());
        List<ProductSkuSupplierRelationSkuOption> skuOptions = currentSku != null && currentSku.getSpuId() != null
            ? resolveSkuOptions(dto.getCorpid(), currentSku.getSpuId())
            : currentSkuOption == null ? List.of() : List.of(currentSkuOption);
        if (skuOptions.isEmpty() && currentSkuOption != null) {
            skuOptions = List.of(currentSkuOption);
        }
        SaveItemVO<SkuSupplierRelationMainDTO> result = SkuSupplierRelationAdminAssembler.buildSaveItemVO(SkuSupplierRelationAdminAssembler.toMainDTO(relation), skuOptions, dto.getCorpid());
        return result;
    }

    @Override
    public Long save(SkuSupplierRelationSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSkuId() == null) {
            throw new BizException("skuId不能为空");
        }
        if (dto.getSupplierId() == null) {
            throw new BizException("supplierId不能为空");
        }
        if (productSkuRepository == null || productSkuRepository.findById(dto.getCorpid(), dto.getSkuId()) == null) {
            throw new BizException("SKU不存在");
        }
        if (supplierRepository == null || supplierRepository.findById(dto.getCorpid(), dto.getSupplierId()) == null) {
            throw new BizException("供应商不存在");
        }
        ProductSkuSupplierRelation relation = SkuSupplierRelationAdminAssembler.toRelation(dto);
        normalizeFlags(relation);
        Long id;
        if (relation.getId() == null) {
            id = relationRepository.save(relation, dto.getUserId());
            relation.setId(id);
            writeHistory(relation, dto.getUserId(), "CREATE_RELATION", dto.getRemark());
        } else {
            ProductSkuSupplierRelation existed = requireRelation(dto.getCorpid(), relation.getId());
            mergeForUpdate(relation, existed);
            relationRepository.update(relation, dto.getUserId());
            id = relation.getId();
            writeHistory(relation, dto.getUserId(), "UPDATE_RELATION", dto.getRemark());
        }
        if (Integer.valueOf(1).equals(relation.getDefaultFlag())) {
            relationRepository.clearDefaultBySkuId(dto.getCorpid(), relation.getSkuId(), relation.getId(), dto.getUserId());
            relationRepository.updateDefaultFlag(dto.getCorpid(), relation.getId(), 1, dto.getUserId());
        }
        return id;
    }

    @Override
    public BaseVO setDefault(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSkuSupplierRelation relation = requireRelation(dto.getCorpid(), dto.getId());
        relationRepository.clearDefaultBySkuId(dto.getCorpid(), relation.getSkuId(), relation.getId(), dto.getUserId());
        relationRepository.updateDefaultFlag(dto.getCorpid(), dto.getId(), 1, dto.getUserId());
        writeHistory(relation, dto.getUserId(), "SET_DEFAULT", null);
        return new BaseVO();
    }

    @Override
    public BaseVO enable(IdBaseDTO dto) {
        updateEnableStatus(dto, 1, "ENABLE_RELATION");
        return new BaseVO();
    }

    @Override
    public BaseVO disable(IdBaseDTO dto) {
        updateEnableStatus(dto, 0, "DISABLE_RELATION");
        return new BaseVO();
    }

    @Override
    public BaseVO delete(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSkuSupplierRelation relation = requireRelation(dto.getCorpid(), dto.getId());
        relationRepository.removeById(dto.getCorpid(), dto.getId(), dto.getUserId());
        writeHistory(relation, dto.getUserId(), "DELETE_RELATION", null);
        return new BaseVO();
    }

    @Override
    public List<SkuSupplierRelationHistoryItemVO> history(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return relationRepository == null ? List.of() : relationRepository.findHistoryByRelationId(dto.getCorpid(), dto.getId()).stream()
            .map(SkuSupplierRelationAdminAssembler::toHistoryItemVO)
            .toList();
    }

    @Override
    public List<SkuSupplierRelationSkuOptionVO> skuOptionsBySpu(SkuSupplierRelationSkuOptionsDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSpuId() == null) {
            throw new BizException("spuId不能为空");
        }
        return resolveSkuOptions(dto.getCorpid(), dto.getSpuId()).stream().map(SkuSupplierRelationAdminAssembler::toSkuOptionVO).toList();
    }

    private void updateEnableStatus(IdBaseDTO dto, Integer enableStatus, String operateType) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSkuSupplierRelation relation = requireRelation(dto.getCorpid(), dto.getId());
        relationRepository.updateEnableStatus(dto.getCorpid(), dto.getId(), enableStatus, dto.getUserId());
        writeHistory(relation, dto.getUserId(), operateType, null);
    }

    private void mergeForUpdate(ProductSkuSupplierRelation target, ProductSkuSupplierRelation existed) {
        target.setCorpid(existed.getCorpid());
        if (target.getSkuId() == null) {
            target.setSkuId(existed.getSkuId());
        }
        if (target.getSupplierId() == null) {
            target.setSupplierId(existed.getSupplierId());
        }
        if (target.getPurchasePrice() == null) {
            target.setPurchasePrice(existed.getPurchasePrice());
        }
        if (target.getDeliveryCycleDay() == null) {
            target.setDeliveryCycleDay(existed.getDeliveryCycleDay());
        }
        if (target.getMinOrderQty() == null) {
            target.setMinOrderQty(existed.getMinOrderQty());
        }
        if (target.getSupplierSkuCode() == null) {
            target.setSupplierSkuCode(existed.getSupplierSkuCode());
        }
        if (target.getDefaultFlag() == null) {
            target.setDefaultFlag(existed.getDefaultFlag());
        }
        if (target.getEnableStatus() == null) {
            target.setEnableStatus(existed.getEnableStatus());
        }
        if (target.getRemark() == null) {
            target.setRemark(existed.getRemark());
        }
    }

    private void normalizeFlags(ProductSkuSupplierRelation relation) {
        if (relation.getDefaultFlag() == null) {
            relation.setDefaultFlag(0);
        }
        if (relation.getEnableStatus() == null) {
            relation.setEnableStatus(1);
        }
    }

    private ProductSkuSupplierRelation requireRelation(String corpid, Long id) {
        ProductSkuSupplierRelation relation = relationRepository == null ? null : relationRepository.findById(corpid, id);
        if (relation == null) {
            throw new BizException("关系不存在");
        }
        return relation;
    }

    private List<ProductSkuSupplierRelationSkuOption> resolveSkuOptions(String corpid, Long spuId) {
        if (relationRepository == null || spuId == null) {
            return List.of();
        }
        return relationRepository.findSkuOptionsBySpu(corpid, spuId);
    }

    private List<ProductSkuSupplierRelationSupplierOption> resolveSupplierOptions(String corpid, Long supplierId) {
        if (relationRepository == null) {
            return List.of();
        }
        return relationRepository.findSupplierOptions(corpid, supplierId, 50);
    }

    private List<ProductSkuSupplierRelationSupplierOption> mergeSupplierOptions(
        List<ProductSkuSupplierRelationSupplierOption> primary,
        List<ProductSkuSupplierRelationSupplierOption> secondary
    ) {
        List<ProductSkuSupplierRelationSupplierOption> merged = new ArrayList<>();
        appendSupplierOptions(merged, primary);
        appendSupplierOptions(merged, secondary);
        return merged;
    }

    private void appendSupplierOptions(List<ProductSkuSupplierRelationSupplierOption> target, List<ProductSkuSupplierRelationSupplierOption> source) {
        for (ProductSkuSupplierRelationSupplierOption item : source) {
            boolean existed = target.stream().anyMatch(option -> Objects.equals(option.getSupplierId(), item.getSupplierId()));
            if (!existed) {
                target.add(item);
            }
        }
    }

    private void writeHistory(ProductSkuSupplierRelation relation, String userId, String operateType, String remark) {
        if (relationRepository == null || relation == null) {
            return;
        }
        ProductSkuSupplierRelationHistory history = new ProductSkuSupplierRelationHistory();
        history.setCorpid(relation.getCorpid());
        history.setRelationId(relation.getId());
        history.setOperateType(operateType);
        history.setOperatorId(userId);
        history.setRemark(remark);
        history.setChangeSnapshot(String.format("skuId=%s,supplierId=%s,defaultFlag=%s,enableStatus=%s", relation.getSkuId(), relation.getSupplierId(), relation.getDefaultFlag(), relation.getEnableStatus()));
        relationRepository.insertHistory(history);
    }
}
