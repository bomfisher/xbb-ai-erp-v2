package xbb.ai.erp.module.product.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.product.admin.dto.ProductUnitCreateDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitUpdateDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.domain.model.ProductUnit;
import xbb.ai.erp.module.product.domain.repository.ProductUnitRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductUnitAppService {

    private final ProductUnitRepository productUnitRepository;

    public Long create(ProductUnitCreateDTO dto) {
        ProductUnit productUnit = new ProductUnit();
        productUnit.setCorpid(dto.getCorpid());
        productUnit.setUnitCode(dto.getUnitCode());
        productUnit.setUnitName(dto.getUnitName());
        productUnit.setPrecisionNum(dto.getPrecisionNum());
        productUnit.setEnableStatus(dto.getEnableStatus());
        return productUnitRepository.save(productUnit, dto.getUserId());
    }

    public void update(ProductUnitUpdateDTO dto) {
        ProductUnit productUnit = new ProductUnit();
        productUnit.setId(dto.getId());
        productUnit.setCorpid(dto.getCorpid());
        productUnit.setUnitCode(dto.getUnitCode());
        productUnit.setUnitName(dto.getUnitName());
        productUnit.setPrecisionNum(dto.getPrecisionNum());
        productUnit.setEnableStatus(dto.getEnableStatus());
        productUnitRepository.update(productUnit, dto.getUserId());
    }

    public void remove(String corpid, Long id, String userId) {
        productUnitRepository.removeById(corpid, id, userId);
    }

    public ProductUnitVO detail(String corpid, Long id) {
        return toVO(productUnitRepository.findById(corpid, id));
    }

    public List<ProductUnitVO> list(ProductUnitListDTO dto) {
        return productUnitRepository.findByCondition(toCondition(dto)).stream().map(this::toVO).toList();
    }

    private Map<String, Object> toCondition(ProductUnitListDTO dto) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("unitCode", dto.getUnitCode());
        condition.put("unitName", dto.getUnitName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        return condition;
    }

    private ProductUnitVO toVO(ProductUnit productUnit) {
        if (productUnit == null) {
            return null;
        }
        ProductUnitVO vo = new ProductUnitVO();
        vo.setId(productUnit.getId());
        vo.setUnitCode(productUnit.getUnitCode());
        vo.setUnitName(productUnit.getUnitName());
        vo.setPrecisionNum(productUnit.getPrecisionNum());
        vo.setEnableStatus(productUnit.getEnableStatus());
        return vo;
    }
}
