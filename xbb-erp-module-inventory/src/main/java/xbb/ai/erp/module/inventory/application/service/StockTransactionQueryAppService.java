package xbb.ai.erp.module.inventory.application.service;
import xbb.ai.erp.base.common.vo.ListBaseVO; import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO; import xbb.ai.erp.module.inventory.admin.vo.StockTransactionQueryItemVO;
public interface StockTransactionQueryAppService { ListBaseVO<StockTransactionQueryItemVO> list(StockTransactionQueryDTO dto); }
