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
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskSaveItemVO;
import xbb.ai.erp.module.purchase.application.service.PurchasePendingTaskAdminAppService;

@RestController
@RequestMapping("/erp/v1/purchase/pending-task")
@RequiredArgsConstructor
public class PurchasePendingTaskAdminController {

    private final PurchasePendingTaskAdminAppService purchasePendingTaskAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<PurchasePendingTaskListItemVO>> list(@RequestBody PurchasePendingTaskListDTO dto) {
        return ResultVO.success(purchasePendingTaskAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<PurchasePendingTaskSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(purchasePendingTaskAdminAppService.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<PurchasePendingTaskSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchasePendingTaskAdminAppService.updateItem(dto));
    }

    @PostMapping("/save")
    public ResultVO<Long> save(@RequestBody PurchasePendingTaskSaveDTO dto) {
        return ResultVO.success(purchasePendingTaskAdminAppService.save(dto));
    }

    @PostMapping("/detail")
    public ResultVO<PurchasePendingTaskDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(purchasePendingTaskAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        purchasePendingTaskAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
