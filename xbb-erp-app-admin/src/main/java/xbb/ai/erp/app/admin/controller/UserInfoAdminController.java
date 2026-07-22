package xbb.ai.erp.app.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class UserInfoAdminController {

    @GetMapping("/erp/v1/user/info")
    public ResultVO<Map<String, Object>> userInfo() {
        return ResultVO.success(Map.of("app", "admin", "status", "ok"));
    }
}
