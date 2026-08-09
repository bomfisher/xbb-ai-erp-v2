package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.application.assembler.ProductCategoryAdminAssembler;
import xbb.ai.erp.module.product.application.service.ProductCategoryAdminAppService;
import xbb.ai.erp.module.product.application.support.ProductAdminParamValidator;
import xbb.ai.erp.module.product.application.support.ProductCategoryFieldEnum;
import xbb.ai.erp.module.product.domain.model.ProductCategory;
import xbb.ai.erp.module.product.domain.repository.ProductCategoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductCategoryAdminAppServiceImpl implements ProductCategoryAdminAppService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryAdminAppServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ListBaseVO<ProductCategoryVO> list(ProductCategoryListDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("categoryCode", dto.getCategoryCode());
        condition.put("categoryName", dto.getCategoryName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        List<ProductCategoryVO> list = productCategoryRepository.findByCondition(condition).stream().map(ProductCategoryAdminAssembler::toVO).toList();
        long total = productCategoryRepository.count(condition);
        ListBaseVO<ProductCategoryVO> vo = new ListBaseVO<>();
//        vo.setHeadList(ProductCategoryFieldEnum.listHead());
        vo.setList(list);
        vo.setPageHelper(new ListBaseVO.PageHelper(resolvePage(dto.getOffset(), dto.getPageSize()), (int) total));
        return vo;
    }

    @Override
    public SaveItemVO<ProductCategorySaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<ProductCategorySaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductCategoryFieldEnum.formHead());
        vo.setData(ProductCategoryAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<ProductCategorySaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<ProductCategorySaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductCategoryFieldEnum.formHead());
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(ProductCategorySaveDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        ProductCategory productCategory = ProductCategoryAdminAssembler.toProductCategory(dto);
        if (productCategory.getId() == null) {
            return productCategoryRepository.save(productCategory, dto.getUserId());
        }
        productCategoryRepository.update(productCategory, dto.getUserId());
        return productCategory.getId();
    }

    @Override
    public ProductCategoryDetailVO detail(IdBaseDTO dto) {
        ProductCategoryDetailVO detailVO = ProductCategoryAdminAssembler.toDetailVO(toSaveItem(dto));
        detailVO.setHeadList(ProductCategoryFieldEnum.formHead());
        return detailVO;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        ProductAdminParamValidator.validateBatchDelete(dto);
        dto.getIdList().forEach(id -> productCategoryRepository.removeById(dto.getCorpid(), id, dto.getUserId()));
    }

    private ProductCategorySaveItemVO toSaveItem(IdBaseDTO dto) {
        ProductAdminParamValidator.validateIdQuery(dto);
        return ProductCategoryAdminAssembler.toSaveItemVO(productCategoryRepository.findById(dto.getCorpid(), dto.getId()));
    }

    private int resolvePage(Integer offset, Integer pageSize) {
        if (offset == null || offset < 0 || pageSize == null || pageSize <= 0) {
            return 1;
        }
        return offset / pageSize + 1;
    }
}
