package xbb.ai.erp.module.masterdata.admin.vo;

import java.util.Map;
import lombok.Data;

@Data
public class ProductSelectOptionVO {
    private Long id;
    private String code;
    private String name;
    private String label;
    private Map<String, Object> linePatch;
}
