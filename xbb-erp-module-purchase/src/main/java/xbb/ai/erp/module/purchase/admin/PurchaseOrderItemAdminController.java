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
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderItemAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase")
@RequiredArgsConstructor
public class PurchaseOrderItemAdminController {

    private final PurchaseOrderItemAdminAppService purchaseOrderItemAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<PurchaseOrderItemListItemVO> list(@RequestBody PurchaseOrderItemListDTO dto) {
        return purchaseOrderItemAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<PurchaseOrderItemSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return purchaseOrderItemAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<PurchaseOrderItemSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return purchaseOrderItemAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody PurchaseOrderItemSaveDTO dto) {
        return purchaseOrderItemAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public PurchaseOrderItemDetailVO detail(@RequestBody IdBaseDTO dto) {
        return purchaseOrderItemAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        purchaseOrderItemAdminAppService.delete(dto);
    }
}
