package xbb.ai.erp.module.common.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;

import java.util.List;

@Data
public class ListMetaBundlePojo {
    private List<FilterField> filterList;
    private List<ListButtonItemPojo> topButtonList;
    private List<ListButtonItemPojo> bottomButtonList;
    private List<ListRowActionItemPojo> rowActionList;
}
