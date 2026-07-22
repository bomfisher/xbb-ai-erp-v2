package xbb.ai.erp.app.mobile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class UserInfoMobileController {

    @GetMapping("/erp/v1/user/info")
    public ResultVO<Map<String, Object>> userInfo() {
        return ResultVO.success(Map.of("app", "mobile", "status", "ok"));
    }
}
