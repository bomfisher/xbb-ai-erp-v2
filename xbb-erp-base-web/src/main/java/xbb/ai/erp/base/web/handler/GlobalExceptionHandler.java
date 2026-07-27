package xbb.ai.erp.base.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ResultVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ResultVO<Void>> handleBizException(BizException exception) {
        return ResponseEntity.badRequest().body(ResultVO.failure(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO<Void>> handleException(Exception exception) {
        log.error("Unhandled exception", exception);
        return ResponseEntity.internalServerError().body(
            ResultVO.failure(CommonErrorCodeEnum.SYSTEM_ERROR.getCode(), CommonErrorCodeEnum.SYSTEM_ERROR.getMessage())
        );
    }
}
