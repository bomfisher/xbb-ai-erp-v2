package xbb.ai.erp.module.product.application.service.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.application.assembler.ProductAdminAssembler;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;
import xbb.ai.erp.module.product.application.port.ProductDraftRepository;
import xbb.ai.erp.module.product.application.validator.ProductSaveBusinessValidator;
import xbb.ai.erp.module.product.application.validator.ProductSaveCommonValidator;
import xbb.ai.erp.module.product.application.validator.ProductSaveProtocolValidator;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductSaveAppServiceImpl implements ProductSaveAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductDraftRepository productDraftRepository;
    private final ProductSaveProtocolValidator protocolValidator;
    private final ProductSaveCommonValidator commonValidator;
    private final ProductSaveBusinessValidator businessValidator;

    @Autowired
    public ProductSaveAppServiceImpl(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository, ProductDraftRepository productDraftRepository) {
        this.productSpuRepository = productSpuRepository;
        this.productSkuRepository = productSkuRepository;
        this.productDraftRepository = productDraftRepository;
        this.protocolValidator = new ProductSaveProtocolValidator();
        this.commonValidator = new ProductSaveCommonValidator();
        this.businessValidator = new ProductSaveBusinessValidator();
    }

    public ProductSaveAppServiceImpl(ProductSpuRepository productSpuRepository, ProductSkuRepository productSkuRepository) {
        this(productSpuRepository, productSkuRepository, null);
    }

    @Override
    public Long save(ProductSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        ProductSaveContextPojo context = ProductAdminAssembler.toSaveContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForSubmit(context);
        businessValidator.validateForSubmit(context);

        ProductSpu productSpu = ProductAdminAssembler.toProductSpu(dto);
        Long spuId = saveSpu(productSpu, dto.getUserId());
        syncSkus(dto.getCorpid(), dto.getUserId(), spuId, dto.getSkus());
        return spuId;
    }

    @Override
    public BaseVO saveAndSubmit(ProductSubmitSaveDTO dto) {
        save(dto);
        if (productDraftRepository != null
            && dto.getDraftMeta() != null
            && dto.getDraftMeta().getDraftCode() != null
            && !dto.getDraftMeta().getDraftCode().isBlank()) {
            productDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    private Long saveSpu(ProductSpu productSpu, String userId) {
        if (productSpuRepository == null || productSpu == null) {
            return productSpu == null ? null : productSpu.getId();
        }
        if (productSpu.getId() == null) {
            return productSpuRepository.save(productSpu, userId);
        }
        productSpuRepository.update(productSpu, userId);
        return productSpu.getId();
    }

    private void syncSkus(String corpid, String userId, Long spuId, List<ProductSkuItemDTO> incomingSkus) {
        if (productSkuRepository == null || spuId == null || incomingSkus == null) {
            return;
        }
        List<ProductSku> existingSkus = productSkuRepository.findBySpuId(corpid, spuId);
        Set<Long> incomingIds = incomingSkus.stream()
            .map(ProductSkuItemDTO::getSkuId)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());
        for (ProductSku existingSku : existingSkus) {
            if (!incomingIds.contains(existingSku.getId())) {
                productSkuRepository.removeById(corpid, existingSku.getId(), userId);
            }
        }
        for (ProductSkuItemDTO skuItem : incomingSkus) {
            ProductSku productSku = ProductAdminAssembler.toProductSku(corpid, spuId, skuItem);
            if (productSku.getId() == null) {
                productSkuRepository.save(productSku, userId);
            } else {
                productSkuRepository.update(productSku, userId);
            }
        }
    }
}
