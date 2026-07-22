package xbb.ai.erp.base.common.exception;

import lombok.Getter;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;

@Getter
public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(CommonErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
    }
}
