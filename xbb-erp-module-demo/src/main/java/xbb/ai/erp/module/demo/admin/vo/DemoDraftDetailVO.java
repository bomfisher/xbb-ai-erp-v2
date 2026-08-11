package xbb.ai.erp.module.demo.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;

@Data
public class DemoDraftDetailVO {
    private String draftCode;
    private DemoMainDTO main;
    private List<DemoItemDTO> items;
    private List<DemoItemDTO> items2;
}
