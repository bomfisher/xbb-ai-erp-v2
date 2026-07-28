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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestItemAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase/request-item")
@RequiredArgsConstructor
public class PurchaseRequestItemAdminController {

    private final PurchaseRequestItemAdminAppService purchaseRequestItemAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseRequestItemListItemVO>> list(@RequestBody PurchaseRequestItemListDTO dto) {
        return ResultVO.success(purchaseRequestItemAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseRequestItemSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseRequestItemAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseRequestItemSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestItemAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchaseRequestItemSaveDTO dto) {
        return ResultVO.success(purchaseRequestItemAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchaseRequestItemDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestItemAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchaseRequestItemAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
