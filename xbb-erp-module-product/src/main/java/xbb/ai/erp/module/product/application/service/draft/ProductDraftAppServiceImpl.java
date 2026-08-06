package xbb.ai.erp.module.product.application.service.draft;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.application.assembler.ProductAdminAssembler;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;
import xbb.ai.erp.module.product.application.port.ProductDraftRepository;
import xbb.ai.erp.module.product.application.validator.ProductSaveCommonValidator;
import xbb.ai.erp.module.product.application.validator.ProductSaveProtocolValidator;

import java.util.List;

@Service
public class ProductDraftAppServiceImpl implements ProductDraftAppService {

    private final ProductDraftRepository productDraftRepository;
    private final ProductSaveProtocolValidator protocolValidator;
    private final ProductSaveCommonValidator commonValidator;

    public ProductDraftAppServiceImpl(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
        this.protocolValidator = new ProductSaveProtocolValidator();
        this.commonValidator = new ProductSaveCommonValidator();
    }

    @Override
    public ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        ProductSaveContextPojo context = ProductAdminAssembler.toSaveContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        ProductSaveDraftPojo draft = ProductAdminAssembler.toDraftPojo(dto);
        productDraftRepository.saveDraft(draft);
        ProductDraftSaveVO vo = new ProductDraftSaveVO();
        vo.setDraftId(draft.getDraftId());
        return vo;
    }

    @Override
    public List<ProductDraftListItemVO> draftList(ProductDraftListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (productDraftRepository == null) {
            return List.of();
        }
        return productDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .filter(draft -> ProductAdminAssembler.matchKeyword(draft, dto.getKeyword()))
            .map(ProductAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public ProductDraftDetailVO loadDraft(ProductDraftLoadDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getDraftId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("draftId不能为空");
        }
        if (productDraftRepository == null) {
            return new ProductDraftDetailVO();
        }
        return ProductAdminAssembler.toDraftDetailVO(productDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftId()));
    }
}
