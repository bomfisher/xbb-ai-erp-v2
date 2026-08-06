package xbb.ai.erp.module.supplier.application.service.draft;

import org.springframework.stereotype.Service;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftSaveVO;
import xbb.ai.erp.module.supplier.application.assembler.SupplierAdminAssembler;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository;
import xbb.ai.erp.module.supplier.application.validator.SupplierSaveCommonValidator;
import xbb.ai.erp.module.supplier.application.validator.SupplierSaveProtocolValidator;

import java.util.List;

@Service
public class SupplierDraftAppServiceImpl implements SupplierDraftAppService {

    private final SupplierDraftRepository supplierDraftRepository;
    private final SupplierSaveProtocolValidator protocolValidator;
    private final SupplierSaveCommonValidator commonValidator;

    public SupplierDraftAppServiceImpl(SupplierDraftRepository supplierDraftRepository) {
        this.supplierDraftRepository = supplierDraftRepository;
        this.protocolValidator = new SupplierSaveProtocolValidator();
        this.commonValidator = new SupplierSaveCommonValidator();
    }

    @Override
    public SupplierDraftSaveVO saveDraft(SupplierDraftSaveDTO dto) {
        SupplierSaveContextPojo context = SupplierAdminAssembler.toDraftContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        SupplierSaveDraftPojo draft = SupplierAdminAssembler.toDraftPojo(dto);
        String draftCode = supplierDraftRepository.saveDraft(draft);
        if (dto.getDraftMeta() != null) {
            dto.getDraftMeta().setDraftCode(draftCode);
        }
        SupplierDraftSaveVO vo = new SupplierDraftSaveVO();
        vo.setDraftCode(draftCode);
        return vo;
    }

    @Override
    public List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto) {
        if (supplierDraftRepository == null) {
            return List.of();
        }
        return supplierDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .map(SupplierAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto) {
        if (supplierDraftRepository == null) {
            return new SupplierDraftDetailVO();
        }
        return SupplierAdminAssembler.toDraftDetailVO(
            supplierDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
        );
    }
}
