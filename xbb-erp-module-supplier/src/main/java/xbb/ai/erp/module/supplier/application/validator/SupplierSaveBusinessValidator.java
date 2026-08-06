package xbb.ai.erp.module.supplier.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;
import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.HashMap;
import java.util.Map;

public class SupplierSaveBusinessValidator {

    private final SupplierRepository supplierRepository;

    public SupplierSaveBusinessValidator(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void validateForSubmit(SupplierSaveContextPojo context) {
        validateSupplierCodeDuplicate(context);
    }

    private void validateSupplierCodeDuplicate(SupplierSaveContextPojo context) {
        if (supplierRepository == null || context == null || context.getMain() == null) {
            return;
        }
        String supplierCode = context.getMain().getSupplierCode();
        if (supplierCode == null || supplierCode.isBlank()) {
            return;
        }
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", context.getCorpid());
        for (Supplier item : supplierRepository.findByCondition(conditionMap)) {
            if (supplierCode.equals(item.getSupplierCode())
                && (context.getMain().getId() == null || !context.getMain().getId().equals(item.getId()))) {
                throw new BizException("供应商编码已存在");
            }
        }
    }
}
