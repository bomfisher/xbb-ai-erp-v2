package xbb.ai.erp.module.common.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.common.admin.dto.MenuListDTO;
import xbb.ai.erp.module.common.admin.vo.MenuListVO;
import xbb.ai.erp.module.common.application.service.MenuService;

@RestController
@RequestMapping("/erp/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/list")
    public ResultVO list(@RequestBody MenuListDTO dto) {
        return ResultVO.success(menuService.list(dto));
    }
}
