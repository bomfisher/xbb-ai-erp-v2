package xbb.ai.erp.module.product.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.product.admin.dto.ProductBrandCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.domain.model.ProductBrand;
import xbb.ai.erp.module.product.domain.repository.ProductBrandRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductBrandAppService {

    private final ProductBrandRepository productBrandRepository;

    public Long create(ProductBrandCreateDTO dto) {
        ProductBrand productBrand = new ProductBrand();
        productBrand.setCorpid(dto.getCorpid());
        productBrand.setBrandCode(dto.getBrandCode());
        productBrand.setBrandName(dto.getBrandName());
        productBrand.setSortNo(dto.getSortNo());
        productBrand.setEnableStatus(dto.getEnableStatus());
        return productBrandRepository.save(productBrand, dto.getUserId());
    }

    public void update(ProductBrandUpdateDTO dto) {
        ProductBrand productBrand = new ProductBrand();
        productBrand.setId(dto.getId());
        productBrand.setCorpid(dto.getCorpid());
        productBrand.setBrandCode(dto.getBrandCode());
        productBrand.setBrandName(dto.getBrandName());
        productBrand.setSortNo(dto.getSortNo());
        productBrand.setEnableStatus(dto.getEnableStatus());
        productBrandRepository.update(productBrand, dto.getUserId());
    }

    public void remove(String corpid, Long id, String userId) {
        productBrandRepository.removeById(corpid, id, userId);
    }

    public ProductBrandVO detail(String corpid, Long id) {
        return toVO(productBrandRepository.findById(corpid, id));
    }

    public List<ProductBrandVO> list(ProductBrandListDTO dto) {
        return productBrandRepository.findByCondition(toCondition(dto)).stream().map(this::toVO).toList();
    }

    private Map<String, Object> toCondition(ProductBrandListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("brandCode", dto.getBrandCode());
        condition.put("brandName", dto.getBrandName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private ProductBrandVO toVO(ProductBrand productBrand) {
        if (productBrand == null) {
            return null;
        }
        ProductBrandVO vo = new ProductBrandVO();
        vo.setId(productBrand.getId());
        vo.setBrandCode(productBrand.getBrandCode());
        vo.setBrandName(productBrand.getBrandName());
        vo.setSortNo(productBrand.getSortNo());
        vo.setEnableStatus(productBrand.getEnableStatus());
        return vo;
    }
}
