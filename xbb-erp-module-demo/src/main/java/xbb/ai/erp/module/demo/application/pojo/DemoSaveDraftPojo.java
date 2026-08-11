package xbb.ai.erp.module.demo.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import java.util.List;

@Data
public class DemoSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private DemoMainDTO main;
    private List<DemoItemDTO> items;
    private List<DemoItemDTO> items2;
    private Long updatedTime;
}
