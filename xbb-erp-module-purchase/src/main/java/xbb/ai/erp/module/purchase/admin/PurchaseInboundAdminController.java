package xbb.ai.erp.module.purchase.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseInboundAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase/inbound")
@RequiredArgsConstructor
public class PurchaseInboundAdminController {

    private final PurchaseInboundAdminAppService purchaseInboundAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseInboundListItemVO>> list(@RequestBody PurchaseInboundListDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseInboundSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseInboundSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchaseInboundSaveDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchaseInboundDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseInboundAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchaseInboundAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
