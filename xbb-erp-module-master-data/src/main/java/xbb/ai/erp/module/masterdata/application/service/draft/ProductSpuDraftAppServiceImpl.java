package xbb.ai.erp.module.masterdata.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftListItemVO;
import xbb.ai.erp.module.masterdata.application.pojo.ProductSpuSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.ProductSpuDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpuDraftAppServiceImpl implements ProductSpuDraftAppService {

    private final ProductSpuDraftRepository repository;
    private final ProductSpuSaveProtocolValidator protocolValidator;
    private final ProductSpuSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(ProductSpuDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        ProductSpuSaveDraftPojo draft = new ProductSpuSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<ProductSpuDraftListItemVO> draftList(ProductSpuDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            ProductSpuDraftListItemVO vo = new ProductSpuDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public ProductSpuDraftDetailVO loadDraft(ProductSpuDraftLoadDTO dto) {
        ProductSpuSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        ProductSpuDraftDetailVO vo = new ProductSpuDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
