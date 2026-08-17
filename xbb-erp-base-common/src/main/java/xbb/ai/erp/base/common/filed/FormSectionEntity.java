package xbb.ai.erp.base.common.filed;

import java.util.List;
import lombok.Data;

@Data
public class FormSectionEntity {
    private String key;
    private String title;
    private Integer order;
    private Integer columns;
    private Boolean collapsed;
    private List<String> fields;
}
