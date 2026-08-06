package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

import java.util.Map;

@Data
public class ProductBusinessSelectOptionVO {

    private Long id;
    private String code;
    private String name;
    private String label;
    private Map<String, Object> linePatch;
}
