package xbb.ai.erp.module.supplier.application.service.delete;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

@Service
public class SupplierDeleteAppServiceImpl implements SupplierDeleteAppService {

    private final SupplierRepository supplierRepository;

    public SupplierDeleteAppServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        supplierRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }
}
