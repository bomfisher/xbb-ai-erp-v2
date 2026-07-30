package xbb.ai.erp.module.common.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.vo.ListBaseVO;

import java.util.List;

@Data
public class ListPageVO<T> {
    private List<T> list;
    private ListBaseVO.PageHelper pageHelper;
}
