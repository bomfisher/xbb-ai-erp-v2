package xbb.ai.erp.module.system.domain.repository;

import xbb.ai.erp.module.system.domain.model.BusinessConfig;

public interface BusinessConfigRepository {

    BusinessConfig findByKey(String corpid, String businessCode);

    void save(BusinessConfig businessConfig);

    void remove(String corpid, String businessCode);
}
