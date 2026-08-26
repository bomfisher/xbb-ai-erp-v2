package xbb.ai.erp.module.inventory.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.inventory.admin.dto.InstantStockQueryDTO;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.InstantStockVO;
import xbb.ai.erp.module.inventory.admin.vo.StockQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockQueryAppService;
import xbb.ai.erp.module.inventory.contract.InventoryStockQueryApi;

@RestController
@RequestMapping("/erp/v1/inventory/stockQuery")
@RequiredArgsConstructor
public class StockQueryAdminController {
    private final StockQueryAppService stockQueryAppService;
    private final InventoryStockQueryApi inventoryStockQueryApi;

    @PostMapping("/instantQty")
    public ResultVO<InstantStockVO> instantQty(@RequestBody InstantStockQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSkuId() == null) {
            throw new BizException("产品不能为空");
        }
        InstantStockVO vo = new InstantStockVO();
        vo.setStockQty(inventoryStockQueryApi.queryInstantQty(dto.getCorpid(), dto.getSkuId(), dto.getWarehouseId()));
        return ResultVO.success(vo);
    }

    @PostMapping("/list")
    public ResultVO<ListBaseVO<StockQueryItemVO>> list(@RequestBody StockQueryDTO dto) {
        return ResultVO.success(stockQueryAppService.list(dto));
    }
}
