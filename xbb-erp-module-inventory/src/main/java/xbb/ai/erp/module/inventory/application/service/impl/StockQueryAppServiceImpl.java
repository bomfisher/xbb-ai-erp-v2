package xbb.ai.erp.module.inventory.application.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockQueryAppService;
import xbb.ai.erp.module.inventory.infrastructure.persistence.mapper.StockBalanceMapper;

@Service @RequiredArgsConstructor
public class StockQueryAppServiceImpl implements StockQueryAppService {
 private final StockBalanceMapper stockBalanceMapper;
 public ListBaseVO<StockQueryItemVO> list(StockQueryDTO dto) { AdminParamValidator.requireCorpid(dto); int page=Math.max(dto.getPageNum()==null?1:dto.getPageNum(),1); int size=Math.min(Math.max(dto.getPageSize()==null?20:dto.getPageSize(),1),200); ListBaseVO<StockQueryItemVO> result=new ListBaseVO<>(); result.setList(stockBalanceMapper.queryList(dto,(page-1)*size,size)); Long count=stockBalanceMapper.queryCount(dto); result.setPageHelper(new ListBaseVO.PageHelper(page,count==null?0:count.intValue())); return result; }
}
