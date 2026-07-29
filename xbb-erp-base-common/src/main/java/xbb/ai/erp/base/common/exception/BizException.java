package xbb.ai.erp.base.common.exception;

import lombok.Getter;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;

@Getter
public class BizException extends RuntimeException {

    private final Integer code;

    private final String message;

    public BizException(CommonErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }

    public BizException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        this.code = CommonErrorCodeEnum.BIZ_ERROR.getCode();
        this.message = message;
    }
}
