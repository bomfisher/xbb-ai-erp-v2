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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseSourceRelationAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase/source-relation")
@RequiredArgsConstructor
public class PurchaseSourceRelationAdminController {

    private final PurchaseSourceRelationAdminAppService purchaseSourceRelationAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchaseSourceRelationListItemVO>> list(@RequestBody PurchaseSourceRelationListDTO dto) {
        return ResultVO.success(purchaseSourceRelationAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchaseSourceRelationSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchaseSourceRelationAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchaseSourceRelationSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseSourceRelationAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchaseSourceRelationSaveDTO dto) {
        return ResultVO.success(purchaseSourceRelationAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchaseSourceRelationDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchaseSourceRelationAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchaseSourceRelationAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
