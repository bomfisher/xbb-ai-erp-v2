package xbb.ai.erp.module.supplier.application.service.save;

import org.springframework.stereotype.Component;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

@Component
public class SupplierOwnerPurchaserResolverImpl implements SupplierOwnerPurchaserResolver {

    private final EmployeeRepositoryImpl employeeRepository;

    public SupplierOwnerPurchaserResolverImpl(EmployeeRepositoryImpl employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public String resolveName(String corpid, String ownerPurchaserId) {
        if (corpid == null || corpid.isBlank() || ownerPurchaserId == null || ownerPurchaserId.isBlank()) {
            return null;
        }
        Employee employee = employeeRepository.findByUserId(corpid, ownerPurchaserId);
        return employee == null ? null : employee.getUserName();
    }
}
