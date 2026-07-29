package xbb.ai.erp.base.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerStructureTest {

    @Test
    void should_define_logger_field_for_unhandled_exception_logging() throws Exception {
        Field logField = GlobalExceptionHandler.class.getDeclaredField("log");
        assertEquals(org.slf4j.Logger.class, logField.getType());
    }

    @Test
    void should_return_fixed_message_for_unhandled_exception() throws Exception {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResultVO<Void> result = handler.handleException(new BizException("客户名称不能为空"));

        Method getCodeMethod = ResultVO.class.getMethod("getCode");
        Method getMessageMethod = ResultVO.class.getMethod("getMessage");

        assertEquals(500, getCodeMethod.invoke(result));
        assertEquals("接口未按预定格式返回，请联系客服", getMessageMethod.invoke(result));
    }

    @Test
    void should_serialize_result_vo_with_base_vo() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ResultVO.success(new BaseVO()));

        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"ok\":1"));
    }
}
