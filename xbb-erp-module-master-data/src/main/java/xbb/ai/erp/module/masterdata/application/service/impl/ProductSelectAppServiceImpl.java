package xbb.ai.erp.module.masterdata.application.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.service.ProductSelectAppService;
import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSkuRepository;

@Service
@RequiredArgsConstructor
public class ProductSelectAppServiceImpl implements ProductSelectAppService {

    private static final String PRODUCT_SKU = "product-sku";
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductSkuRepository productSkuRepository;

    @Override
    public List<ProductSelectOptionVO> quickSearch(ProductSelectQueryDTO dto) {
        validate(dto);
        return productSkuRepository.findByCondition(conditionMap(dto, null, DEFAULT_PAGE_SIZE)).stream()
            .map(this::toOption)
            .toList();
    }

    @Override
    public ListBaseVO<ProductSelectOptionVO> dialogSearch(ProductSelectQueryDTO dto) {
        validate(dto);
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? DEFAULT_PAGE_SIZE : dto.getPageSize();
        Map<String, Object> conditionMap = conditionMap(dto, pageNum, pageSize);
        ListBaseVO<ProductSelectOptionVO> result = new ListBaseVO<>();
        result.setList(productSkuRepository.findByCondition(conditionMap).stream().map(this::toOption).toList());
        Long total = productSkuRepository.count(conditionMap);
        result.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return result;
    }

    @Override
    public ProductSelectOptionVO getById(ProductSelectQueryDTO dto) {
        validate(dto);
        if (dto.getId() == null) {
            return null;
        }
        ProductSku sku = productSkuRepository.findById(dto.getCorpid(), dto.getId());
        return sku == null || !Integer.valueOf(1).equals(sku.getEnabled()) ? null : toOption(sku);
    }

    private void validate(ProductSelectQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
//        if (!PRODUCT_SKU.equals(dto.getProductType())) {
//            throw new BizException("不支持的产品选择类型");
//        }
//        if (dto.getBusinessCode() == null || dto.getBusinessCode().isBlank()) {
//            throw new BizException("业务编码不能为空");
//        }
    }

    private Map<String, Object> conditionMap(ProductSelectQueryDTO dto, Integer pageNum, Integer pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("enabled", 1);
        conditions.put("keyword", dto.getKeyword() == null ? null : dto.getKeyword().trim());
        if (pageNum != null) {
            conditions.put("pageNum", pageNum);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private ProductSelectOptionVO toOption(ProductSku sku) {
        ProductSelectOptionVO option = new ProductSelectOptionVO();
        option.setId(sku.getId());
        option.setCode(sku.getSkuCode());
        option.setName(sku.getSkuName());
        option.setLabel(sku.getSkuCode() + " - " + sku.getSkuName());
        return option;
    }
}
