package xbb.ai.erp.base.common.filed;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FieldEntity {
    private String attr;
    private String attrName;
    private String fieldType;
    private Integer required;
    private Integer editable;
    private List<FieldItem> itemList;
    private List<FieldEntity> subField;
    private BusinessSelectConfig businessSelectConfig;
    private SelectionFillConfig selectionFillConfig;
    private ProductSelectConfig productSelectConfig;

    @Data
    public static class BusinessSelectConfig {
        private String businessType;
        private String productType;
        private String businessCode;
        private String quickSearchUrl;
        private String dialogSearchUrl;
        private String getByIdUrl;
        private Map<String, Object> requestPayload;
        private String placeholder;
        private String dialogTitle;
        private Boolean multiple;
    }

    @Data
    public static class SelectionFillConfig {
        private Boolean enabled;
    }

    @Data
    public static class ProductSelectConfig {
        private String productType;
        private String businessCode;
        private String sourceMode;
        private String defaultSource;
        private Map<String, Object> requestPayload;
        private String placeholder;
        private String dialogTitle;
        private Boolean multiple;
    }
}
