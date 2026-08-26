package xbb.ai.erp.module.system.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.system.domain.model.BusinessConfig;
import xbb.ai.erp.module.system.domain.repository.BusinessConfigRepository;
import xbb.ai.erp.module.system.infrastructure.persistence.mapper.BusinessConfigMapper;
import xbb.ai.erp.module.system.infrastructure.persistence.po.BusinessConfigPO;

@Repository
@RequiredArgsConstructor
public class BusinessConfigRepositoryImpl implements BusinessConfigRepository {

    private final BusinessConfigMapper mapper;

    @Override
    public BusinessConfig findByKey(String corpid, String businessCode) {
        BusinessConfigPO po = mapper.findByKey(corpid, businessCode);
        return po == null ? null : new BusinessConfig(po.getCorpid(), po.getBusinessCode(), po.getConfigJson());
    }

    @Override
    public void save(BusinessConfig businessConfig) {
        BusinessConfigPO po = mapper.findByKey(businessConfig.corpid(), businessConfig.businessCode());
        long now = System.currentTimeMillis();
        if (po == null) {
            po = new BusinessConfigPO();
            po.setCorpid(businessConfig.corpid());
            po.setBusinessCode(businessConfig.businessCode());
            po.setAddTime(now);
            po.setDel(0);
        }
        po.setConfigJson(businessConfig.configJson());
        po.setUpdateTime(now);
        if (po.getId() == null) {
            mapper.insert(po);
        } else {
            mapper.update(po);
        }
    }

    @Override
    public void remove(String corpid, String businessCode) {
        mapper.remove(corpid, businessCode, System.currentTimeMillis());
    }
}
