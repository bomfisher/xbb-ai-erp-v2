package xbb.ai.erp.module.system.contract;

public interface BusinessConfigQueryApi {

    <T> T get(String corpid, BusinessConfigKey<T> configKey);
}
