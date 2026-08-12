package xbb.ai.erp.module.masterdata.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.assembler.WarehouseAdminAssembler;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseValidator;
import xbb.ai.erp.module.masterdata.application.port.WarehouseDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.domain.model.Warehouse;
import xbb.ai.erp.module.masterdata.domain.repository.WarehouseRepository;

@Service
@RequiredArgsConstructor
public class WarehouseSaveAppServiceImpl {

    private final WarehouseRepository warehouseRepository;

    private final WarehouseDraftRepository draftRepository;
    private final WarehouseSaveProtocolValidator protocolValidator;
    private final WarehouseSaveCommonValidator commonValidator;
    private final WarehouseSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(WarehouseSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(WarehouseSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        WarehouseValidator.validateSave(dto);
        Warehouse entity = WarehouseAdminAssembler.toWarehouse(dto);
        if (entity.getId() == null) {
            return warehouseRepository.insert(entity);
        }
        warehouseRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            warehouseRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
