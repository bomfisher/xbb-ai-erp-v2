package xbb.ai.erp.module.inventory.application.service;

import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockQueryItemVO;
public interface StockQueryAppService { ListBaseVO<StockQueryItemVO> list(StockQueryDTO dto); }
