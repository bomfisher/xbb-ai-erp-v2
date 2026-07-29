package xbb.ai.erp.base.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ResultVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String UNEXPECTED_MESSAGE = "接口未按预定格式返回，请联系客服";

    @ExceptionHandler(BizException.class)
    public ResultVO<Void> handleBizException(BizException exception) {
        return ResultVO.failure(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception exception) {
        log.error("Unhandled exception", exception);
        return ResultVO.failure(CommonErrorCodeEnum.SYSTEM_ERROR.getCode(), UNEXPECTED_MESSAGE);
    }
}
