package xbb.ai.erp.module.product.application.service.query;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.application.assembler.ProductAdminAssembler;
import xbb.ai.erp.module.product.application.support.ProductFieldEnum;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductQueryAppServiceImpl implements ProductQueryAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    public ProductQueryAppServiceImpl(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository) {
        this.productSpuRepository = productSpuRepository;
        this.productSkuRepository = productSkuRepository;
    }

    public static ProductQueryAppServiceImpl forTesting(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository) {
        return new ProductQueryAppServiceImpl(productSpuRepository, productSkuRepository);
    }

    @Override
    public ListBaseVO<ProductListItemVO> list(ProductListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "keyword", dto.getKeyword());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPage());
        conditionMap = QueryConditionMapHelper.prepare(conditionMap);
        List<ProductSpu> list = productSpuRepository == null ? List.of() : productSpuRepository.findByCondition(conditionMap);
        long total = productSpuRepository == null ? 0L : productSpuRepository.count(conditionMap);
        ListBaseVO<ProductListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(ProductAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPage() == null ? 1 : dto.getPage(), (int) total));
        return vo;
    }

    @Override
    public SaveItemVO<ProductSaveItemVO> addItem() {
        SaveItemVO<ProductSaveItemVO> vo = ProductAdminAssembler.buildEmptySaveItemVO();
        vo.setHeadList(ProductFieldEnum.formHead());
        return vo;
    }

    @Override
    public SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSpu productSpu = productSpuRepository == null ? null : productSpuRepository.findById(dto.getCorpid(), dto.getId());
        List<ProductSku> productSkuList = productSkuRepository == null ? List.of() : productSkuRepository.findBySpuId(dto.getCorpid(), dto.getId());
        SaveItemVO<ProductSaveItemVO> vo = ProductAdminAssembler.toSaveItemVO(productSpu, productSkuList);
        vo.setHeadList(ProductFieldEnum.formHead());
        return vo;
    }

    @Override
    public List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto);
    }

    @Override
    public ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<ProductBusinessSelectOptionVO> all = findBusinessSelectOptions(dto);
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<ProductBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setHeadList(buildBusinessSelectHeadList(dto.getBusinessCode()));
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    @Override
    public ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("id不能为空");
        }
        ProductSku productSku = productSkuRepository == null ? null : productSkuRepository.findById(dto.getCorpid(), dto.getId());
        if (productSku == null) {
            return null;
        }
        return applyBusinessLinePatch(ProductAdminAssembler.toBusinessSelectOptionVO(productSku), dto.getBusinessCode());
    }

    @Override
    public ProductDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ProductSpu productSpu = productSpuRepository == null ? null : productSpuRepository.findById(dto.getCorpid(), dto.getId());
        List<ProductSku> productSkuList = productSkuRepository == null ? List.of() : productSkuRepository.findBySpuId(dto.getCorpid(), dto.getId());
        return ProductAdminAssembler.toDetailVO(productSpu, productSkuList);
    }

    private List<ProductBusinessSelectOptionVO> findBusinessSelectOptions(ProductBusinessSelectQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "keyword", dto.getKeyword());
        conditionMap.put("canPurchase", 1);
        conditionMap.put("enableStatus", 1);
        return (productSkuRepository == null ? List.<ProductSku>of() : productSkuRepository.findByCondition(conditionMap)).stream()
            .map(ProductAdminAssembler::toBusinessSelectOptionVO)
            .map(option -> applyBusinessLinePatch(option, dto.getBusinessCode()))
            .toList();
    }

    private ProductBusinessSelectOptionVO applyBusinessLinePatch(ProductBusinessSelectOptionVO option, String businessCode) {
        Map<String, Object> linePatch = new HashMap<>(option.getLinePatch());
        linePatch.put("purchaseUnitId", 1L);
        if ("PURCHASE_REQUEST".equals(businessCode)) {
            linePatch.put("requestQty", BigDecimal.ONE);
        }
        if ("PURCHASE_ORDER".equals(businessCode)) {
            linePatch.put("warehouseId", 1L);
            linePatch.put("orderQty", BigDecimal.ONE);
            linePatch.put("grossPrice", BigDecimal.ZERO);
            linePatch.put("netPrice", BigDecimal.ZERO);
            linePatch.put("taxRate", BigDecimal.ZERO);
            linePatch.put("taxAmount", BigDecimal.ZERO);
            linePatch.put("grossAmount", BigDecimal.ZERO);
            linePatch.put("netAmount", BigDecimal.ZERO);
        }
        option.setLinePatch(linePatch);
        return option;
    }

    private List<FieldEntity> buildBusinessSelectHeadList(String businessCode) {
        List<FieldEntity> headList = new ArrayList<>();
        headList.add(buildBusinessSelectHeader("code", "SKU编码"));
        headList.add(buildBusinessSelectHeader("name", "SKU名称"));
        headList.add(buildBusinessSelectHeader("specSnapshot", "规格"));
        headList.add(buildBusinessSelectHeader("purchaseUnitId", "采购单位"));
        if ("PURCHASE_REQUEST".equals(businessCode)) {
            headList.add(buildBusinessSelectHeader("requestQty", "申请数量"));
        }
        if ("PURCHASE_ORDER".equals(businessCode)) {
            headList.add(buildBusinessSelectHeader("warehouseId", "收货仓库"));
            headList.add(buildBusinessSelectHeader("grossPrice", "含税单价"));
            headList.add(buildBusinessSelectHeader("taxRate", "税率"));
        }
        return headList;
    }

    private FieldEntity buildBusinessSelectHeader(String attr, String attrName) {
        FieldEntity field = new FieldEntity();
        field.setAttr(attr);
        field.setAttrName(attrName);
        field.setFieldType("1");
        field.setRequired(0);
        field.setEditable(0);
        field.setItemList(List.of());
        return field;
    }
}
