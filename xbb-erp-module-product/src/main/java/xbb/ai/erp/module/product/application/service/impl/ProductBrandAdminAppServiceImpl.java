package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.application.assembler.ProductBrandAdminAssembler;
import xbb.ai.erp.module.product.application.service.ProductBrandAdminAppService;
import xbb.ai.erp.module.product.application.support.ProductAdminParamValidator;
import xbb.ai.erp.module.product.application.support.ProductBrandFieldEnum;
import xbb.ai.erp.module.product.domain.model.ProductBrand;
import xbb.ai.erp.module.product.domain.repository.ProductBrandRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductBrandAdminAppServiceImpl implements ProductBrandAdminAppService {

    private final ProductBrandRepository productBrandRepository;

    public ProductBrandAdminAppServiceImpl(ProductBrandRepository productBrandRepository) {
        this.productBrandRepository = productBrandRepository;
    }

    @Override
    public ListBaseVO<ProductBrandVO> list(ProductBrandListDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("brandCode", dto.getBrandCode());
        condition.put("brandName", dto.getBrandName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        List<ProductBrandVO> list = productBrandRepository.findByCondition(condition).stream().map(ProductBrandAdminAssembler::toVO).toList();
        long total = productBrandRepository.count(condition);
        ListBaseVO<ProductBrandVO> vo = new ListBaseVO<>();
        vo.setHeadList(ProductBrandFieldEnum.listHead());
        vo.setList(list);
        vo.setPageHelper(new ListBaseVO.PageHelper(resolvePage(dto.getOffset(), dto.getPageSize()), (int) total));
        return vo;
    }

    @Override
    public SaveItemVO<ProductBrandSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<ProductBrandSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductBrandFieldEnum.formHead());
        vo.setData(ProductBrandAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<ProductBrandSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<ProductBrandSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductBrandFieldEnum.formHead());
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(ProductBrandSaveDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        ProductBrand productBrand = ProductBrandAdminAssembler.toProductBrand(dto);
        if (productBrand.getId() == null) {
            return productBrandRepository.save(productBrand, dto.getUserId());
        }
        productBrandRepository.update(productBrand, dto.getUserId());
        return productBrand.getId();
    }

    @Override
    public ProductBrandDetailVO detail(IdBaseDTO dto) {
        ProductBrandDetailVO detailVO = ProductBrandAdminAssembler.toDetailVO(toSaveItem(dto));
        detailVO.setHeadList(ProductBrandFieldEnum.formHead());
        return detailVO;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        ProductAdminParamValidator.validateBatchDelete(dto);
        dto.getIdList().forEach(id -> productBrandRepository.removeById(dto.getCorpid(), id, dto.getUserId()));
    }

    private ProductBrandSaveItemVO toSaveItem(IdBaseDTO dto) {
        ProductAdminParamValidator.validateIdQuery(dto);
        return ProductBrandAdminAssembler.toSaveItemVO(productBrandRepository.findById(dto.getCorpid(), dto.getId()));
    }

    private int resolvePage(Integer offset, Integer pageSize) {
        if (offset == null || offset < 0 || pageSize == null || pageSize <= 0) {
            return 1;
        }
        return offset / pageSize + 1;
    }
}
