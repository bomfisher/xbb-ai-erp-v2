package xbb.ai.erp.module.demo.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;

@Data
public class DemoSaveItemVO {
    private DemoMainDTO main;
    private java.util.List<DemoItemDTO> items;
}
