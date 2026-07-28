package xbb.ai.erp.module.common.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
import xbb.ai.erp.module.common.application.service.ListCommonService;

@RestController
@RequestMapping("/erp/v1/common/list")
@RequiredArgsConstructor
public class ListCommonController {

    private final ListCommonService listCommonService;

    @PostMapping("/filter")
    public ResultVO<ListFilterVO> filter(@RequestBody ListCommonQueryDTO dto) {
        return ResultVO.success(listCommonService.filter(dto));
    }

    @PostMapping("/header")
    public ResultVO<ListHeaderVO> header(@RequestBody ListCommonQueryDTO dto) {
        return ResultVO.success(listCommonService.header(dto));
    }

    @PostMapping("/topButton")
    public ResultVO<ListTopButtonVO> topButton(@RequestBody ListCommonQueryDTO dto) {
        return ResultVO.success(listCommonService.topButton(dto));
    }

    @PostMapping("/bottomButton")
    public ResultVO<ListBottomButtonVO> bottomButton(@RequestBody ListCommonQueryDTO dto) {
        return ResultVO.success(listCommonService.bottomButton(dto));
    }
}
