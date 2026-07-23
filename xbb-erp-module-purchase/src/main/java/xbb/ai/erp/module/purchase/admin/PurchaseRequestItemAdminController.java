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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestItemAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase")
@RequiredArgsConstructor
public class PurchaseRequestItemAdminController {

    private final PurchaseRequestItemAdminAppService purchaseRequestItemAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<PurchaseRequestItemListItemVO> list(@RequestBody PurchaseRequestItemListDTO dto) {
        return purchaseRequestItemAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<PurchaseRequestItemSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return purchaseRequestItemAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<PurchaseRequestItemSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return purchaseRequestItemAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody PurchaseRequestItemSaveDTO dto) {
        return purchaseRequestItemAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public PurchaseRequestItemDetailVO detail(@RequestBody IdBaseDTO dto) {
        return purchaseRequestItemAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        purchaseRequestItemAdminAppService.delete(dto);
    }
}
