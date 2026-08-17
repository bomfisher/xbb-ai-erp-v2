package xbb.ai.erp.module.system.domain.repository;

public interface BizNoCounterRepository {

    Long reserve(String corpid, String businessCode, String periodKey, int size);
}
