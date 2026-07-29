package xbb.ai.erp.module.common.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;

import java.util.List;

@Data
public class ListRowActionVO {
    private List<ListRowActionItemPojo> list;
}
