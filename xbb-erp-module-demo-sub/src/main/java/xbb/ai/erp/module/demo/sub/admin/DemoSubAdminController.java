package xbb.ai.erp.module.demo.sub.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftListDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftLoadDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSubmitSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSelectionFillDTO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDraftDetailVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDraftListItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubListItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSaveItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSelectionFillVO;
import xbb.ai.erp.module.demo.sub.application.service.DemoSubAdminAppService;

@RestController
@RequestMapping("/erp/v1/demo-sub")
@RequiredArgsConstructor
public class DemoSubAdminController {

  private final DemoSubAdminAppService demoSubAdminAppService;

  @PostMapping("/list")
  public ResultVO<ListBaseVO<DemoSubListItemVO>> list(@RequestBody ListBaseDTO dto) {
    return ResultVO.success(demoSubAdminAppService.list(dto));
  }

  @PostMapping("/addItem")
  public ResultVO<SaveItemVO<DemoSubSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
    return ResultVO.success(demoSubAdminAppService.addItem(dto));
  }

  @PostMapping("/updateItem")
  public ResultVO<SaveItemVO<DemoSubSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
    return ResultVO.success(demoSubAdminAppService.updateItem(dto));
  }

  @PostMapping("/selectionFill")
  public ResultVO<DemoSubSelectionFillVO> selectionFill(@RequestBody DemoSubSelectionFillDTO dto) {
    return ResultVO.success(demoSubAdminAppService.selectionFill(dto));
  }

  @PostMapping("/saveDraft")
  public ResultVO<DraftSaveVO> saveDraft(@RequestBody DemoSubDraftSaveDTO dto) {
    return ResultVO.success(demoSubAdminAppService.saveDraft(dto));
  }

  @PostMapping("/saveAndSubmit")
  public ResultVO<BaseVO> saveAndSubmit(@RequestBody DemoSubSubmitSaveDTO dto) {
    return ResultVO.success(demoSubAdminAppService.saveAndSubmit(dto));
  }

  @PostMapping("/draftList")
  public ResultVO<List<DemoSubDraftListItemVO>> draftList(@RequestBody DemoSubDraftListDTO dto) {
    return ResultVO.success(demoSubAdminAppService.draftList(dto));
  }

  @PostMapping("/loadDraft")
  public ResultVO<DemoSubDraftDetailVO> loadDraft(@RequestBody DemoSubDraftLoadDTO dto) {
    return ResultVO.success(demoSubAdminAppService.loadDraft(dto));
  }
}
