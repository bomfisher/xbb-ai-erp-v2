package xbb.ai.erp.module.system.contract;

import xbb.ai.erp.base.common.module.BusinessCodeEnum;

public interface BusinessConfigKey<T> {

    String code();

    BusinessCodeEnum businessCode();

    Class<T> valueType();

    T defaultValue();
}
