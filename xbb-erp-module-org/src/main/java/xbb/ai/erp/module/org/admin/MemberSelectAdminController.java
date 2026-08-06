package xbb.ai.erp.module.org.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.org.admin.dto.MemberSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.MemberSelectOptionVO;
import xbb.ai.erp.module.org.application.service.MemberSelectAppService;

import java.util.List;

@RestController
@RequestMapping("/erp/v1/org/memberSelect")
@RequiredArgsConstructor
public class MemberSelectAdminController {

    private final MemberSelectAppService memberSelectAppService;

    @PostMapping("/quickSearch")
    public ResultVO<List<MemberSelectOptionVO>> quickSearch(@RequestBody MemberSelectQueryDTO dto) {
        return ResultVO.success(memberSelectAppService.quickSearch(dto));
    }

    @PostMapping("/dialogSearch")
    public ResultVO<ListBaseVO<MemberSelectOptionVO>> dialogSearch(@RequestBody MemberSelectQueryDTO dto) {
        return ResultVO.success(memberSelectAppService.dialogSearch(dto));
    }

    @PostMapping("/getById")
    public ResultVO<MemberSelectOptionVO> getById(@RequestBody MemberSelectQueryDTO dto) {
        return ResultVO.success(memberSelectAppService.getById(dto));
    }
}
