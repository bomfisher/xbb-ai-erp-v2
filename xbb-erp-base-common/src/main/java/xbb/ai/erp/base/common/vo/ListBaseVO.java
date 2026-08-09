package xbb.ai.erp.base.common.vo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

@Data
public class ListBaseVO<T> {
//    private List<FieldEntity> headList;

    private List<T> list;

    private PageHelper pageHelper;

    @Data
    public static class PageHelper {
        private Integer page;

        private Integer count;

        private Boolean hasLeft;

        private Boolean hasRight;

        public PageHelper(Integer page, Integer count) {
            this.page = page;
            this.count = count;
            this.hasLeft = page > 1;
            this.hasRight = count > page;
        }
    }
}
