package xbb.ai.erp.module.inventory.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xbb.ai.erp.base.common.vo.*;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockTransactionQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockTransactionQueryAppService;

@RestController
@RequestMapping("/erp/v1/inventory/stockTransactionQuery")
@RequiredArgsConstructor
public class StockTransactionQueryAdminController {
    private final StockTransactionQueryAppService service;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<StockTransactionQueryItemVO>> list(@RequestBody StockTransactionQueryDTO dto) {
        return ResultVO.success(service.list(dto));
    }
}
