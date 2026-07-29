package xbb.ai.erp.base.web.handler;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalErrorControllerStructureTest {

    @Test
    void should_reset_http_status_and_return_fixed_message() throws Exception {
        GlobalErrorController controller = new GlobalErrorController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);

        ResultVO<Void> result = controller.handleError(request, response);

        Method getCodeMethod = ResultVO.class.getMethod("getCode");
        Method getMessageMethod = ResultVO.class.getMethod("getMessage");
        assertEquals(200, response.getStatus());
        assertEquals(500, getCodeMethod.invoke(result));
        assertEquals("接口未按预定格式返回，请联系客服", getMessageMethod.invoke(result));
    }
}
