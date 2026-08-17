package xbb.ai.erp.module.inventory.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockQueryAppService;

@RestController @RequestMapping("/erp/v1/inventory/stockQuery") @RequiredArgsConstructor
public class StockQueryAdminController {
    private final StockQueryAppService stockQueryAppService;
    @PostMapping("/list") public ResultVO<ListBaseVO<StockQueryItemVO>> list(@RequestBody StockQueryDTO dto) { return ResultVO.success(stockQueryAppService.list(dto)); }
}
