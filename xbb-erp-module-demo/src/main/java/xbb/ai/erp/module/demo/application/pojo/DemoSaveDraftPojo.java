package xbb.ai.erp.module.demo.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;

@Data
public class DemoSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private DemoMainDTO main;
    private Long updatedTime;
}
