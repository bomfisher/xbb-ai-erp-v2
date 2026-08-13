package xbb.ai.erp.module.masterdata.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.assembler.ProductSpuAdminAssembler;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuValidator;
import xbb.ai.erp.module.masterdata.application.port.ProductSpuDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;
import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSpuRepository;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSkuRepository;

@Service
@RequiredArgsConstructor
public class ProductSpuSaveAppServiceImpl {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    private final ProductSpuDraftRepository draftRepository;
    private final ProductSpuSaveProtocolValidator protocolValidator;
    private final ProductSpuSaveCommonValidator commonValidator;
    private final ProductSpuSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(ProductSpuSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        Long productSpuId = save(dto);
        if (dto.getMain().getId() == null) {
            productSkuRepository.insert(buildDefaultSku(dto, productSpuId));
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    private ProductSku buildDefaultSku(ProductSpuSaveDTO dto, Long productSpuId) {
        ProductSku productSku = new ProductSku();
        productSku.setCorpid(dto.getCorpid());
        productSku.setSpuId(productSpuId);
        productSku.setSkuCode(dto.getMain().getSpuCode());
        productSku.setSkuName(dto.getMain().getSpuName());
        productSku.setUnitName("件");
        productSku.setEnabled(dto.getMain().getEnabled());
        productSku.setRemark(dto.getMain().getRemark());
        productSku.setCreatorId(dto.getUserId());
        productSku.setModifyId(dto.getUserId());
        return productSku;
    }

    public Long save(ProductSpuSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        ProductSpuValidator.validateSave(dto);
        ProductSpu entity = ProductSpuAdminAssembler.toProductSpu(dto);
        entity.setModifyId(dto.getUserId());
        if (entity.getId() == null) {
            entity.setCreatorId(dto.getUserId());
            return productSpuRepository.insert(entity);
        }
        productSpuRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            productSpuRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
