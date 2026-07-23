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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase")
@RequiredArgsConstructor
public class PurchaseRequestAdminController {

    private final PurchaseRequestAdminAppService purchaseRequestAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<PurchaseRequestListItemVO> list(@RequestBody PurchaseRequestListDTO dto) {
        return purchaseRequestAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<PurchaseRequestSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return purchaseRequestAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<PurchaseRequestSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return purchaseRequestAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody PurchaseRequestSaveDTO dto) {
        return purchaseRequestAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public PurchaseRequestDetailVO detail(@RequestBody IdBaseDTO dto) {
        return purchaseRequestAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        purchaseRequestAdminAppService.delete(dto);
    }
}
