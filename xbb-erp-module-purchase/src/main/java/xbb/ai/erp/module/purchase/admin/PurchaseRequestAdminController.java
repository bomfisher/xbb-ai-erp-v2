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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase/request")
@RequiredArgsConstructor
public class PurchaseRequestAdminController {

    private final PurchaseRequestAdminAppService purchaseRequestAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseRequestListItemVO>> list(@RequestBody PurchaseRequestListDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchaseRequestSaveDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchaseRequestDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseRequestAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchaseRequestAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
