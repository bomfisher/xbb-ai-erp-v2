package xbb.ai.erp.module.masterdata.domain.repository;

import java.util.List;
import xbb.ai.erp.module.masterdata.domain.model.CustomerContact;

public interface CustomerContactRepository {
    List<CustomerContact> findByCustomerId(String corpid, Long customerId);

    void sync(String corpid, Long customerId, List<CustomerContact> contacts);
}
