package xbb.ai.erp.module.masterdata.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.assembler.SupplierAdminAssembler;
import xbb.ai.erp.module.masterdata.application.validator.SupplierValidator;
import xbb.ai.erp.module.masterdata.application.port.SupplierDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.SupplierSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.application.validator.SupplierSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.SupplierSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;

@Service
@RequiredArgsConstructor
public class SupplierSaveAppServiceImpl {

    private final SupplierRepository supplierRepository;

    private final SupplierDraftRepository draftRepository;
    private final SupplierSaveProtocolValidator protocolValidator;
    private final SupplierSaveCommonValidator commonValidator;
    private final SupplierSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(SupplierSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SupplierValidator.validateSave(dto);
        Supplier entity = SupplierAdminAssembler.toSupplier(dto);
        if (entity.getId() == null) {
            return supplierRepository.insert(entity);
        }
        supplierRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            supplierRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
