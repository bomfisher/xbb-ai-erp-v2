package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.application.assembler.ProductUnitAdminAssembler;
import xbb.ai.erp.module.product.application.service.ProductUnitAdminAppService;
import xbb.ai.erp.module.product.application.support.ProductAdminParamValidator;
import xbb.ai.erp.module.product.application.support.ProductUnitFieldEnum;
import xbb.ai.erp.module.product.domain.model.ProductUnit;
import xbb.ai.erp.module.product.domain.repository.ProductUnitRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductUnitAdminAppServiceImpl implements ProductUnitAdminAppService {

    private final ProductUnitRepository productUnitRepository;

    public ProductUnitAdminAppServiceImpl(ProductUnitRepository productUnitRepository) {
        this.productUnitRepository = productUnitRepository;
    }

    @Override
    public ListBaseVO<ProductUnitVO> list(ProductUnitListDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("unitCode", dto.getUnitCode());
        condition.put("unitName", dto.getUnitName());
        condition.put("offset", dto.getOffset());
        condition.put("pageSize", dto.getPageSize());
        List<ProductUnitVO> list = productUnitRepository.findByCondition(condition).stream().map(ProductUnitAdminAssembler::toVO).toList();
        long total = productUnitRepository.count(condition);
        ListBaseVO<ProductUnitVO> vo = new ListBaseVO<>();
//        vo.setHeadList(ProductUnitFieldEnum.listHead());
        vo.setList(list);
        vo.setPageHelper(new ListBaseVO.PageHelper(resolvePage(dto.getOffset(), dto.getPageSize()), (int) total));
        return vo;
    }

    @Override
    public SaveItemVO<ProductUnitSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<ProductUnitSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductUnitFieldEnum.formHead());
        vo.setData(ProductUnitAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<ProductUnitSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<ProductUnitSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(ProductUnitFieldEnum.formHead());
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(ProductUnitSaveDTO dto) {
        ProductAdminParamValidator.requireCorpid(dto);
        ProductUnit productUnit = ProductUnitAdminAssembler.toProductUnit(dto);
        if (productUnit.getId() == null) {
            return productUnitRepository.save(productUnit, dto.getUserId());
        }
        productUnitRepository.update(productUnit, dto.getUserId());
        return productUnit.getId();
    }

    @Override
    public ProductUnitDetailVO detail(IdBaseDTO dto) {
        ProductUnitDetailVO detailVO = ProductUnitAdminAssembler.toDetailVO(toSaveItem(dto));
        detailVO.setHeadList(ProductUnitFieldEnum.formHead());
        return detailVO;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        ProductAdminParamValidator.validateBatchDelete(dto);
        dto.getIdList().forEach(id -> productUnitRepository.removeById(dto.getCorpid(), id, dto.getUserId()));
    }

    private ProductUnitSaveItemVO toSaveItem(IdBaseDTO dto) {
        ProductAdminParamValidator.validateIdQuery(dto);
        return ProductUnitAdminAssembler.toSaveItemVO(productUnitRepository.findById(dto.getCorpid(), dto.getId()));
    }

    private int resolvePage(Integer offset, Integer pageSize) {
        if (offset == null || offset < 0 || pageSize == null || pageSize <= 0) {
            return 1;
        }
        return offset / pageSize + 1;
    }
}
