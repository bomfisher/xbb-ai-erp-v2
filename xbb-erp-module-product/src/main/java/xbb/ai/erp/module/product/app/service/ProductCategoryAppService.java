package xbb.ai.erp.module.product.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.domain.model.ProductCategory;
import xbb.ai.erp.module.product.domain.repository.ProductCategoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductCategoryAppService {

    private final ProductCategoryRepository productCategoryRepository;

    public Long create(ProductCategoryCreateDTO dto) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCorpid(dto.getCorpid());
        productCategory.setCategoryCode(dto.getCategoryCode());
        productCategory.setCategoryName(dto.getCategoryName());
        productCategory.setParentId(dto.getParentId());
        productCategory.setCategoryLevel(dto.getCategoryLevel());
        productCategory.setSortNo(dto.getSortNo());
        productCategory.setEnableStatus(dto.getEnableStatus());
        return productCategoryRepository.save(productCategory, dto.getUserId());
    }

    public void update(ProductCategoryUpdateDTO dto) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(dto.getId());
        productCategory.setCorpid(dto.getCorpid());
        productCategory.setCategoryCode(dto.getCategoryCode());
        productCategory.setCategoryName(dto.getCategoryName());
        productCategory.setParentId(dto.getParentId());
        productCategory.setCategoryLevel(dto.getCategoryLevel());
        productCategory.setSortNo(dto.getSortNo());
        productCategory.setEnableStatus(dto.getEnableStatus());
        productCategoryRepository.update(productCategory, dto.getUserId());
    }

    public void remove(String corpid, Long id, String userId) {
        productCategoryRepository.removeById(corpid, id, userId);
    }

    public ProductCategoryVO detail(String corpid, Long id) {
        return toVO(productCategoryRepository.findById(corpid, id));
    }

    public List<ProductCategoryVO> list(ProductCategoryListDTO dto) {
        return productCategoryRepository.findByCondition(toCondition(dto)).stream().map(this::toVO).toList();
    }

    private Map<String, Object> toCondition(ProductCategoryListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("categoryCode", dto.getCategoryCode());
        condition.put("categoryName", dto.getCategoryName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private ProductCategoryVO toVO(ProductCategory productCategory) {
        if (productCategory == null) {
            return null;
        }
        ProductCategoryVO vo = new ProductCategoryVO();
        vo.setId(productCategory.getId());
        vo.setCategoryCode(productCategory.getCategoryCode());
        vo.setCategoryName(productCategory.getCategoryName());
        vo.setParentId(productCategory.getParentId());
        vo.setCategoryLevel(productCategory.getCategoryLevel());
        vo.setSortNo(productCategory.getSortNo());
        vo.setEnableStatus(productCategory.getEnableStatus());
        return vo;
    }
}
