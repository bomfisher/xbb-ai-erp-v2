package xbb.ai.erp.base.web.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.enums.CommonErrorCodeEnum;
import xbb.ai.erp.base.common.vo.ResultVO;

@RestController
public class GlobalErrorController implements ErrorController {

    private static final String UNEXPECTED_MESSAGE = "接口未按预定格式返回，请联系客服";

    @RequestMapping("/error")
    public ResultVO<Void> handleError(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode instanceof Integer status && status < 400) {
            return ResultVO.failure(status, UNEXPECTED_MESSAGE);
        }
        return ResultVO.failure(CommonErrorCodeEnum.SYSTEM_ERROR.getCode(), UNEXPECTED_MESSAGE);
    }
}
