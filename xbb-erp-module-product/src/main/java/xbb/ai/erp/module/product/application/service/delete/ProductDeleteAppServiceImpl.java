package xbb.ai.erp.module.product.application.service.delete;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

@Service
public class ProductDeleteAppServiceImpl implements ProductDeleteAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    public ProductDeleteAppServiceImpl(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository) {
        this.productSpuRepository = productSpuRepository;
        this.productSkuRepository = productSkuRepository;
    }

    public static ProductDeleteAppServiceImpl forTesting(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository) {
        return new ProductDeleteAppServiceImpl(productSpuRepository, productSkuRepository);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        if (productSpuRepository == null || productSkuRepository == null) {
            return;
        }
        for (Long id : dto.getIdList()) {
            productSkuRepository.removeBySpuId(dto.getCorpid(), id, dto.getUserId());
            productSpuRepository.removeById(dto.getCorpid(), id, dto.getUserId());
        }
    }
}
