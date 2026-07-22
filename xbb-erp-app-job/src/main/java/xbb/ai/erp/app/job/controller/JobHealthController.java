package xbb.ai.erp.app.job.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.util.Map;

@RestController
public class JobHealthController {

    @GetMapping("/erp/v1/job/health")
    public ResultVO<Map<String, Object>> health() {
        return ResultVO.success(Map.of("app", "job", "status", "ok"));
    }
}
