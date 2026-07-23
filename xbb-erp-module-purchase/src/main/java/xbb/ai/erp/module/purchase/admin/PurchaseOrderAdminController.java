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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase")
@RequiredArgsConstructor
public class PurchaseOrderAdminController {

    private final PurchaseOrderAdminAppService purchaseOrderAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<PurchaseOrderListItemVO> list(@RequestBody PurchaseOrderListDTO dto) {
        return purchaseOrderAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<PurchaseOrderSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return purchaseOrderAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<PurchaseOrderSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return purchaseOrderAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody PurchaseOrderSaveDTO dto) {
        return purchaseOrderAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public PurchaseOrderDetailVO detail(@RequestBody IdBaseDTO dto) {
        return purchaseOrderAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        purchaseOrderAdminAppService.delete(dto);
    }
}
