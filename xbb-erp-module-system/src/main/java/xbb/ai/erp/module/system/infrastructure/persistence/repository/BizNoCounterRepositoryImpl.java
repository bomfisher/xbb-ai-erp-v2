package xbb.ai.erp.module.system.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.module.system.domain.repository.BizNoCounterRepository;
import xbb.ai.erp.module.system.infrastructure.persistence.mapper.BizNoCounterMapper;

@Repository
@RequiredArgsConstructor
public class BizNoCounterRepositoryImpl implements BizNoCounterRepository {

    private final BizNoCounterMapper mapper;

    @Override
    @Transactional
    public Long reserve(String corpid, String businessCode, String periodKey, int size) {
        mapper.insertIgnore(corpid, businessCode, periodKey);
        mapper.reserve(corpid, businessCode, periodKey, size);
        return mapper.currentReservedValue();
    }
}
